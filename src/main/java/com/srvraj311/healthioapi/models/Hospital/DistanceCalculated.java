package com.srvraj311.healthioapi.models.Hospital;


public class DistanceCalculated {
    private String distance;
    private String time;
    private String timeTraffic;

    public DistanceCalculated(String distance, String time, String timeTraffic) {
        this.distance = distance;
        this.time = time;
        this.timeTraffic = timeTraffic;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeTraffic() {
        return timeTraffic;
    }

    public void setTimeTraffic(String timeTraffic) {
        this.timeTraffic = timeTraffic;
    }
}
