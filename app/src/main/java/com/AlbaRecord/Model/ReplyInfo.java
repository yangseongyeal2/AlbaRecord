package com.AlbaRecord.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class ReplyInfo {
    private String uid;
    // private String created_at;
    private String deleted_at;
    private String content;
    @ServerTimestamp
    private Date date;
    private String documentId;
    private List<String> uidLikelist;

    public ReplyInfo(String uid, String deleted_at, String content, Date date, String documentId, List<String> uidLikelist) {
        this.uid = uid;
        this.deleted_at = deleted_at;
        this.content = content;
        this.date = date;
        this.documentId = documentId;
        this.uidLikelist = uidLikelist;
    }

    public List<String> getUidLikelist() {
        return uidLikelist;
    }

    public void setUidLikelist(List<String> uidLikelist) {
        this.uidLikelist = uidLikelist;
    }

    @Override
    public String toString() {
        return "ReplyInfo{" +
                "uid='" + uid + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", documentId='" + documentId + '\'' +
                ", uidLikelist=" + uidLikelist +
                '}';
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public ReplyInfo() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }
}
