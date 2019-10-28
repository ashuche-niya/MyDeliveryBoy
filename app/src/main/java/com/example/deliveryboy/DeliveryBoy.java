package com.example.deliveryboy;

import java.io.Serializable;

public class DeliveryBoy implements Serializable {
    String boyname;
    String boyphoneno;

    public DeliveryBoy(String boyname, String boyphoneno) {
        this.boyname = boyname;
        this.boyphoneno = boyphoneno;
    }

    public DeliveryBoy() {
    }

    public String getBoyname() {
        return boyname;
    }

    public void setBoyname(String boyname) {
        this.boyname = boyname;
    }

    public String getBoyphoneno() {
        return boyphoneno;
    }

    public void setBoyphoneno(String boyphoneno) {
        this.boyphoneno = boyphoneno;
    }
}
