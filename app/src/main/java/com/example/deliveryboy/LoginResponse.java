package com.example.deliveryboy;

public class LoginResponse {

    private String del_boy_phone, del_boy_id, found;

    public LoginResponse(String del_boy_phone, String del_boy_id, String found) {
        this.del_boy_phone = del_boy_phone;
        this.del_boy_id = del_boy_id;
        this.found = found;
    }

    public String getDel_boy_phone() {
        return del_boy_phone;
    }

    public void setDel_boy_phone(String del_boy_phone) {
        this.del_boy_phone = del_boy_phone;
    }

    public String getDel_boy_id() {
        return del_boy_id;
    }

    public void setDel_boy_id(String del_boy_id) {
        this.del_boy_id = del_boy_id;
    }

    public String getFound() {
        return found;
    }

    public void setFound(String found) {
        this.found = found;
    }
}
