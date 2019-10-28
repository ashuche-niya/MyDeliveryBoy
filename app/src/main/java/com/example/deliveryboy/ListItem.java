package com.example.deliveryboy;

public class ListItem {

    private String pickUpAddr, checkpointAddr, customerAddr;

    public ListItem(String pickUpAddr, String checkpointAddr, String customerAddr) {
        this.pickUpAddr = pickUpAddr;
        this.checkpointAddr = checkpointAddr;
        this.customerAddr = customerAddr;
    }

    public String getPickUpAddr() {
        return pickUpAddr;
    }

    public void setPickUpAddr(String pickUpAddr) {
        this.pickUpAddr = pickUpAddr;
    }

    public String getCheckpointAddr() {
        return checkpointAddr;
    }

    public void setCheckpointAddr(String checkpointAddr) {
        this.checkpointAddr = checkpointAddr;
    }

    public String getCustomerAddr() {
        return customerAddr;
    }

    public void setCustomerAddr(String customerAddr) {
        this.customerAddr = customerAddr;
    }
}
