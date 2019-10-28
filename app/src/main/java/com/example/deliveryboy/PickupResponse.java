package com.example.deliveryboy;

public class PickupResponse {

    private String success;

    public PickupResponse(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
