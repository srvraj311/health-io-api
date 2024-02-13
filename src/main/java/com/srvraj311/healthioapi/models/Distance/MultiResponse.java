package com.srvraj311.healthioapi.models.Distance;

import java.util.List;

public class MultiResponse {
    private String user_cord;
    private List<String> hospital_cord;

    public MultiResponse(String user_cord, List<String> hospital_cord) {
        this.user_cord = user_cord;
        this.hospital_cord = hospital_cord;
    }

    public String getUser_cord() {
        return user_cord;
    }

    public void setUser_cord(String user_cord) {
        this.user_cord = user_cord;
    }

    public List<String> getHospital_cord() {
        return hospital_cord;
    }

    public void setHospital_cord(List<String> hospital_cord) {
        this.hospital_cord = hospital_cord;
    }
}
