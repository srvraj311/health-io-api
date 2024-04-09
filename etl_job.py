import json
import sys

import pandas as pd
import requests


def clear_last_line():
    """Clears the last line of the console output."""
    # Platform-specific code to clear the last line
    if sys.platform.startswith('win'):
        print("\r" + " " * len(last_printed_line) + "\r", end="")
    else:
        sys.stdout.write("\033[K")  # Clear to end of line for Unix-like systems


def print_progress(completed, total, update_frequency=1):
    global last_printed_line
    if completed % update_frequency == 0:
        percentage = completed / total * 100
        progress_line = f"Progress: {percentage:.2f}% ({completed} out of {total})"
        print('\r' + progress_line, end="")  # Print without line break
        last_printed_line = progress_line
    clear_last_line()  # Clear the previous line for update effect


df = pd.read_csv('hospital_directory.csv', low_memory=False)
unique_specialities = df['Specialties'].unique()
# unique_specialities
hospitals_final = []
for index, row in df.iterrows():
    type = ''
    if row.get('Hospital_category'):
        type = row.get('Hospital_category')
    elif row.get('Hospital_Care_Type'):
        type = row.get('Hospital_Care_Type')

    geolocation = ''
    if row.get('Location_Coordinates') not in ['', 'nan', 'NaN', None]:
        geolocation = str(row.get('Location_Coordinates'))
    else:
        geolocation = None

    hospital = {
        'name': row.Hospital_Name if row.Hospital_Name != 'nan' else '',
        'city': row.District if row.District != 'nan' else '',
        'id': 'HIO' + str(row.State_ID) + str(row.District_ID) + str(row.Sr_No) if row.Sr_No != 'nan' else '',
        'geolocation': geolocation if geolocation is not None else str(''),
        'type': row.get('Hospital_Care_Type') if row.get('Hospital_Category') else '',
        'opening_time': None,
        'closing_time': None
    }
    hospitalInfo = {
        'hospital_id': 'HIO' + str(row.State_ID) + str(row.District_ID) + str(row.Sr_No),
        'address': row.Location if row.Location != 'nan' else '',
        'city_name': row.District if row.District != 'nan' else '',
        'state_name': row.State if row.State != 'nan' else '',
        'geolocation': geolocation
    }
    bloodBank = {
        'hospital_id': 'HIO' + str(row.State_ID) + str(row.District_ID) + str(row.Sr_No),
    }
    availability = {
        'hospital_id': 'HIO' + str(row.State_ID) + str(row.District_ID) + str(row.Sr_No),
        'bed': 0,
        'total_bed': 0,
        'icu': 0,
        'total_icu': 0,
        'ccu': 0,
        'total_ccu': 0,
        'ventilator': 0,
        'total_ventilator': 0,
        'oxygen_cylinders': 0,
        'total_oxygen_cylinders': 0
    }
    amenities = {
        'hospital_id': 'HIO' + str(row.State_ID) + str(row.District_ID) + str(row.Sr_No),
        "x_ray": False,
        "mri": False,
        "ecg": False,
        "ultra_sound": False,
        "blood_test": False
    }

    row = {
        'hospital': hospital,
        'hospitalInfo': hospitalInfo,
        'bloodBank': bloodBank,
        'availability': availability,
        'amenities': amenities
    }
    hospitals_final.append(row)

base_url = "http://localhost:8080/api/v1/hospital"
headers = {
    "Content-Type": "application/json",
    "Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzb3VyYWJocmFqMzExQGdtYWlsLmNvbSIsImlhdCI6MTcxMjQ2NzY2MCwiZXhwIjoxNzEyNTAzNjYwfQ.3Uax_f3UtDaaExKrK4Lcy4iveZJYOAyXMOcXiaFW5O8"
}

total_calls = len(hospitals_final)
completed_calls = 0
last_printed_line = ""

failed = []

# Add all hospitals
for index in range(0, len(hospitals_final)):
    try:
        completed_calls += 1
        print_progress(completed_calls, total_calls)
        payload = hospitals_final[index]["hospital"]
        # payload = json.dumps(payload, default=str, allow_nan=True)
        # print(payload)
        # Add hospital
        response = requests.post(base_url + '/add', json=payload, headers=headers)
        if response.status_code == 200:
            data = response.json()
            # print("Hospital updated for id: " + str(index))
        else:
            data = response.text
            # print(f"Error: API request failed with status code {response.status_code}")

        # Update hospital info
        payload = hospitals_final[index]["hospitalInfo"]
        response = requests.post(base_url + '/update?command=hospital_info', json=payload, headers=headers)
        if response.status_code == 200:
            data = response.json()
            # print("Hospital info updated for id: " + str(index))
        else:
            data = response.text
            # print(f"Error: API request failed with status code {response.status_code}")
    except Exception as e:
        print("Error: " + str(e))
        failed.append(index)

print("Failed: " + str(failed))
failed = {'failed': failed}
failed = df.DataFrame(failed)
failed.to_json('failed.json', index=False)