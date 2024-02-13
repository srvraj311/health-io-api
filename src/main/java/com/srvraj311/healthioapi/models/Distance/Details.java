package com.srvraj311.healthioapi.models.Distance;

public class Details {
    String travelDistance;
    String travelDuration;
    String travelMode;

    public Details(String travelDistance, String travelDuration, String travelMode) {
        this.travelDistance = travelDistance;
        this.travelDuration = travelDuration;
        this.travelMode = travelMode;
    }

    public String getTravelDistance() {
        return travelDistance;
    }

    public void setTravelDistance(String travelDistance) {
        this.travelDistance = travelDistance;
    }

    public String getTravelDuration() {
        return travelDuration;
    }

    public void setTravelDuration(String travelDuration) {
        this.travelDuration = travelDuration;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    @Override
    public String toString() {
        return "Details{" +
                "travelDistance='" + travelDistance + '\'' +
                ", travelDuration='" + travelDuration + '\'' +
                ", travelMode='" + travelMode + '\'' +
                '}';
    }
}
