package com.AlbaRecord.Model;

public class EvaluateModel {
    private int diligence,flexibility,mastery,attitude,communication;
    private String hashtag,hashtagdetail,date_text;

    public EvaluateModel(int diligence, int flexibility, int mastery, int attitude, int communication, String hashtag, String hashtagdetail, String date_text) {
        this.diligence = diligence;
        this.flexibility = flexibility;
        this.mastery = mastery;
        this.attitude = attitude;
        this.communication = communication;
        this.hashtag = hashtag;
        this.hashtagdetail = hashtagdetail;
        this.date_text = date_text;
    }

    public int getDiligence() {
        return diligence;
    }

    public void setDiligence(int diligence) {
        this.diligence = diligence;
    }

    public int getFlexibility() {
        return flexibility;
    }

    public void setFlexibility(int flexibility) {
        this.flexibility = flexibility;
    }

    public int getMastery() {
        return mastery;
    }

    public void setMastery(int mastery) {
        this.mastery = mastery;
    }

    public int getAttitude() {
        return attitude;
    }

    public void setAttitude(int attitude) {
        this.attitude = attitude;
    }

    public int getCommunication() {
        return communication;
    }

    public void setCommunication(int communication) {
        this.communication = communication;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }


    public String getHashtagdetail() {
        return hashtagdetail;
    }

    public void setHashtagdetail(String hashtagdetail) {
        this.hashtagdetail = hashtagdetail;
    }

    public String getDate_text() {
        return date_text;
    }

    public void setDate_text(String date_text) {
        this.date_text = date_text;
    }

    @Override
    public String toString() {
        return "EvaluateModel{" +
                "diligence=" + diligence +
                ", flexibility=" + flexibility +
                ", mastery=" + mastery +
                ", attitude=" + attitude +
                ", communication=" + communication +
                ", hashtag='" + hashtag + '\'' +
                ", hashtagdetail='" + hashtagdetail + '\'' +
                ", date_text='" + date_text + '\'' +
                '}';
    }
}
