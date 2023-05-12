package com.example.expenseplanner;

public class ReadWriteUserInfo {

    public String name,dob,gender,mobile;

    //constructor
    public ReadWriteUserInfo(){

    }
    public ReadWriteUserInfo(String fullName, String dob, String textGender, String mobile) {
        this.name=fullName;
        this.dob=dob;
        this.gender=textGender;
        this.mobile=mobile;
    }
}
