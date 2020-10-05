package com.AlbaRecord.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class NotiInfo {
    private String title;
    private String content;
    private String documentId;
    @ServerTimestamp
    private Date date;

    public NotiInfo(){}
    public NotiInfo(String title, String content, String documentId,Date date) {
        this.title = title;
        this.content = content;
        this.documentId = documentId;
        this.date=date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "NotiInfo{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", documentId='" + documentId + '\'' +
                ", date=" + date +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
