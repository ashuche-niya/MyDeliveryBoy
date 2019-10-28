package com.example.deliveryboy;

import android.app.Application;

public class UserClient extends Application {
    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    String phoneno="";

    public UserClient(String phoneno) {
        this.phoneno = phoneno;
    }

    public UserClient() {
    }
}
