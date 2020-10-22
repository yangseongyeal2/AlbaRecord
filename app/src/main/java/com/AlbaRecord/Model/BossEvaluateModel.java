package com.AlbaRecord.Model;

import java.util.ArrayList;

public class BossEvaluateModel {
    private ArrayList<String> strList;
    private String date_text;

    @Override
    public String toString() {
        return "BossEvaluateModel{" +
                "strList=" + strList +
                ", date_text='" + date_text + '\'' +
                '}';
    }

    public String getDate_text() {
        return date_text;
    }

    public void setDate_text(String date_text) {
        this.date_text = date_text;
    }

    public BossEvaluateModel(ArrayList<String> strList, String date_text) {
        this.strList = strList;
        this.date_text = date_text;
    }

    public BossEvaluateModel() {

    }

    public ArrayList<String> getStrList() {
        return strList;
    }

    public void setStrList(ArrayList<String> strList) {
        this.strList = strList;
    }
}
