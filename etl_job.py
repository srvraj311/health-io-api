import pandas as pd;
df = pd.read_csv('hospital_directory.csv', low_memory=False)
unique_specialities = df['Specialties'].unique()
# unique_specialities
hospitals_final = []
for index, row in df.iterrows():
    cityName = str(row.District).split(' ')
    hospital = {
        'name' : row.Hospital_Name,
        'city': ''.join(cityName),
        'id' : 'HIO' + str(row.State) + str(row.District) + str(row.Sr_No),
        'geolocation': row.Location_Coordinates,
        'type': row.get('Hospital_Care_Type') if row.get('Hospital_Category') else '',
        'opening_time': None,
        'closing_time': None
    }
    hospitalInfo = {
        'address' : row.Address_Original_First_Line,
        'city_name' : row.District,
        'state_name' : row.State,
        'geolocation' : row.Location_Coordinates
    }

    row = {
        'hospital' : hospital,
        'hospitalInfo' : hospitalInfo
    }
    hospitals_final.append(row)