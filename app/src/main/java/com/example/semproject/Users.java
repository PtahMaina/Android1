package com.example.semproject;


public class Users {
    String email,fullname,phone;



    public Users() {
    }

    public Users(String email, String fullname, String phone) {
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
