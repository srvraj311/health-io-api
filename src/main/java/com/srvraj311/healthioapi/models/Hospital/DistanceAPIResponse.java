package com.srvraj311.healthioapi.models.Hospital;



import com.srvraj311.healthioapi.models.Distance.Res;

import java.util.List;

public class DistanceAPIResponse {
    String authenticationResultCode;
    List<Res> resourceSets;

    public DistanceAPIResponse(String authenticationResultCode, List<Res> resourceSets) {
        this.authenticationResultCode = authenticationResultCode;
        this.resourceSets = resourceSets;
    }

    public String getAuthenticationResultCode() {
        return authenticationResultCode;
    }

    public void setAuthenticationResultCode(String authenticationResultCode) {
        this.authenticationResultCode = authenticationResultCode;
    }

    public List<Res> getResourceSets() {
        return resourceSets;
    }

    public void setResourceSets(List<Res> resourceSets) {
        this.resourceSets = resourceSets;
    }
}