package com.AlbaRecord.Model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    private String Email;
    private String PassWord;
    private String phonenumber;
    private String name;
    private String age;
    private String adress;
    private String degree;
    private double latitude;
    private double logtitude;
    private String photo;
    private ArrayList<String> licenses;
    private String self_introduce;
    int flag;
    private String DocumentId;
    private ArrayList<String> MyBoss;
    private String Account;
    private String Bank;

    public EmployeeModel(String email, String passWord, String phonenumber, String name, String age, String adress, String degree, double latitude, double logtitude, String photo, ArrayList<String> licenses, String self_introduce, int flag, String documentId, ArrayList<String> myBoss, String account, String bank) {
        Email = email;
        PassWord = passWord;
        this.phonenumber = phonenumber;
        this.name = name;
        this.age = age;
        this.adress = adress;
        this.degree = degree;
        this.latitude = latitude;
        this.logtitude = logtitude;
        this.photo = photo;
        this.licenses = licenses;
        this.self_introduce = self_introduce;
        this.flag = flag;
        DocumentId = documentId;
        MyBoss = myBoss;
        Account = account;
        Bank = bank;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getBank() {
        return Bank;
    }

    public void setBank(String bank) {
        Bank = bank;
    }

    @Override
    public String toString() {
        return "EmployeeModel{" +
                "Email='" + Email + '\'' +
                ", PassWord='" + PassWord + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", adress='" + adress + '\'' +
                ", degree='" + degree + '\'' +
                ", latitude=" + latitude +
                ", logtitude=" + logtitude +
                ", photo='" + photo + '\'' +
                ", licenses=" + licenses +
                ", self_introduce='" + self_introduce + '\'' +
                ", flag=" + flag +
                ", DocumentId='" + DocumentId + '\'' +
                ", MyBoss=" + MyBoss +
                ", Account='" + Account + '\'' +
                ", Bank='" + Bank + '\'' +
                '}';
    }

    public EmployeeModel(String email, String passWord, String phonenumber, String name, String age, String adress, String degree, double latitude, double logtitude, String photo, ArrayList<String> licenses, String self_introduce, int flag, String documentId, ArrayList<String> myBoss) {
        Email = email;
        PassWord = passWord;
        this.phonenumber = phonenumber;
        this.name = name;
        this.age = age;
        this.adress = adress;
        this.degree = degree;
        this.latitude = latitude;
        this.logtitude = logtitude;
        this.photo = photo;
        this.licenses = licenses;
        this.self_introduce = self_introduce;
        this.flag = flag;
        DocumentId = documentId;
        MyBoss = myBoss;
    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }

    public int getFlag() {
        return flag;
    }

    public ArrayList<String> getMyBoss() {
        return MyBoss;
    }

    public void setMyBoss(ArrayList<String> myBoss) {
        MyBoss = myBoss;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


    public ArrayList<String> getLicenses() {
        return licenses;
    }

    public void setLicenses(ArrayList<String> licenses) {
        this.licenses = licenses;
    }

    public String getSelf_introduce() {
        return self_introduce;
    }

    public void setSelf_introduce(String self_introduce) {
        this.self_introduce = self_introduce;
    }

    public String getPhoto() {
        return photo;
    }

    public EmployeeModel() {
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }



    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLogtitude() {
        return logtitude;
    }

    public void setLogtitude(double logtitude) {
        this.logtitude = logtitude;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

}
