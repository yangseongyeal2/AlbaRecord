package com.AlbaRecord.Model;

import java.util.ArrayList;

public class UserModel {
    private String userEmail;
    private String userPassword;
    private String phoneNumber;
    public String nickname;
    private ArrayList<String> favoritList;

    public ArrayList<String> getFavoritList() {
        return favoritList;
    }

    public void setFavoritList(ArrayList<String> favoritList) {
        this.favoritList = favoritList;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nickname='" + nickname + '\'' +
                ", favoritList=" + favoritList +
                '}';
    }

}

