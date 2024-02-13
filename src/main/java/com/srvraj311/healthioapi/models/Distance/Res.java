package com.srvraj311.healthioapi.models.Distance;



import java.util.List;

public class Res {
    List<Details> resources;

    public Res(List<Details> resources) {
        this.resources = resources;
    }

    public List<Details> getResources() {
        return resources;
    }

    public void setResources(List<Details> resources) {
        this.resources = resources;
    }
}
