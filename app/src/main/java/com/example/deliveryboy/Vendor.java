package com.example.deliveryboy;

public class Vendor {

    private String vname;
    private boolean isChecked;

    public Vendor(String vname, boolean isChecked) {
        this.vname = vname;
        this.isChecked = isChecked;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
