package com.AlbaRecord.Model;

import java.util.ArrayList;

public class BossModel {
    private String email ;
    private String password ;
    private String phoneNumber;
    private String name;
    private String brand;
    private String address;
    private String businessNum;
    private double latitude;
    private double longtitude;
    int flag;//0 : 사장님 1: 직원 2: 관리자
    private String DocumentId;
    private ArrayList<String>MyEmployee;
    public ArrayList<String> getDocumentIdList() {
        return MyEmployee;
    }
    private ArrayList<BoardInfo> boardInfoList;

    public ArrayList<BoardInfo> getBoardInfoList() {
        return boardInfoList;
    }

    public void setBoardInfoList(ArrayList<BoardInfo> boardInfoList) {
        this.boardInfoList = boardInfoList;
    }

    public ArrayList<String> getMyEmployee() {
        return MyEmployee;
    }

    public void setMyEmployee(ArrayList<String> myEmployee) {
        MyEmployee = myEmployee;
    }

    public BossModel(String email, String password, String phoneNumber, String name, String brand, String address, String businessNum, double latitude, double longtitude, int flag, String documentId, ArrayList<String> myEmployee) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.brand = brand;
        this.address = address;
        this.businessNum = businessNum;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.flag = flag;
        DocumentId = documentId;
        MyEmployee = myEmployee;
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
                ", latitude=" + latitude +
                ", longtitude=" + longtitude +
                ", flag=" + flag +
                ", DocumentId='" + DocumentId + '\'' +
                ", MyEmployee=" + MyEmployee +
                '}';
    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }

    public void setDocumentIdList(ArrayList<String> documentIdList) {
        MyEmployee = documentIdList;
    }



    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public BossModel(String email
            , String password
            , String phoneNumber
            , String name
            , String brand
            , String address
            , String businessNum
            , int flag
            , double latitude
            , double longtitude) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.brand = brand;
        this.address = address;
        this.businessNum = businessNum;
        this.flag=flag;
        this.latitude=latitude;
        this.longtitude=longtitude;
    }
    public BossModel(){}


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

