package com.AlbaRecord.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class EvaluateModel  implements Parcelable {
    private int diligence,flexibility,mastery,attitude,communication;
    private String hashtag,hashtagdetail,date_text;
    private String brandname,careerthing;

    public EvaluateModel(int diligence, int flexibility, int mastery, int attitude, int communication, String hashtag, String hashtagdetail, String date_text, String brandname, String careerthing) {
        this.diligence = diligence;
        this.flexibility = flexibility;
        this.mastery = mastery;
        this.attitude = attitude;
        this.communication = communication;
        this.hashtag = hashtag;
        this.hashtagdetail = hashtagdetail;
        this.date_text = date_text;
        this.brandname = brandname;
        this.careerthing = careerthing;
    }

    public EvaluateModel() {

    }

    protected EvaluateModel(Parcel in) {
        diligence = in.readInt();
        flexibility = in.readInt();
        mastery = in.readInt();
        attitude = in.readInt();
        communication = in.readInt();
        hashtag = in.readString();
        hashtagdetail = in.readString();
        date_text = in.readString();
        brandname = in.readString();
        careerthing = in.readString();
    }

    public static final Creator<EvaluateModel> CREATOR = new Creator<EvaluateModel>() {
        @Override
        public EvaluateModel createFromParcel(Parcel in) {
            return new EvaluateModel(in);
        }

        @Override
        public EvaluateModel[] newArray(int size) {
            return new EvaluateModel[size];
        }
    };

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
                ", brandname='" + brandname + '\'' +
                ", careerthing='" + careerthing + '\'' +
                '}';
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getCareerthing() {
        return careerthing;
    }

    public void setCareerthing(String careerthing) {
        this.careerthing = careerthing;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeInt(this.diligence);
        dest.writeInt(this.flexibility);
        dest.writeInt(this.mastery);
        dest.writeInt(this.attitude);
        dest.writeInt(this.communication);
        dest.writeString(this.hashtag);
        dest.writeString(this.hashtagdetail);
        dest.writeString(this.date_text);
        dest.writeString(this.brandname);
        dest.writeString(this.careerthing);

    }
}
