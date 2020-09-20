package com.AlbaRecord.Model;

import java.util.ArrayList;

public class UserModel {
    private String email ;
    private String password ;
    private String phoneNumber;
    private String name;
    private String brand;
    private String address;
    private String businessNum;
    int flag;//0 : 사장님 1: 직원 2: 관리자

    public int getFlag() {
        return flag;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", address='" + address + '\'' +
                ", businessNum='" + businessNum + '\'' +
                ", flag=" + flag +
                '}';
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public UserModel(String email
            , String password
            , String phoneNumber
            , String name
            , String brand
            , String address
            , String businessNum
            ,int flag) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.brand = brand;
        this.address = address;
        this.businessNum = businessNum;
        this.flag=flag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessNum() {
        return businessNum;
    }

    public void setBusinessNum(String businessNum) {
        this.businessNum = businessNum;
    }
}

