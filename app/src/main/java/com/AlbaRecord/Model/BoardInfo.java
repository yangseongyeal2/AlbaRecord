package com.AlbaRecord.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BoardInfo {
    private String title;
    private String content;
    private String uid;
    private String documentId;
    private String deleted_at;
    @ServerTimestamp
    private Date date;
    private List<String> uidList;
    private int viewcount;
    private int replycount;
    private ArrayList<String> mDownloadURIList;
    private String nickname;
    private String BoardName;

    public String getBoardName() {
        return BoardName;
    }

    public void setBoardName(String boardName) {
        BoardName = boardName;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "BoardInfo{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", uid='" + uid + '\'' +
                ", documentId='" + documentId + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                ", date=" + date +
                ", uidList=" + uidList +
                ", viewcount=" + viewcount +
                ", replycount=" + replycount +
                ", mDownloadURIList=" + mDownloadURIList +
                ", nickname='" + nickname + '\'' +
                ", BoardName='" + BoardName + '\'' +
                '}';
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BoardInfo(String title, String content, String uid, String documentId, Date date, String deleted_at, List<String> uidList, int viewcount, int replycount, ArrayList<String> mDownloadURIList, String nickname, String boardName) {
        this.title = title;
        this.content = content;
        this.uid = uid;
        this.documentId = documentId;
        this.deleted_at = deleted_at;
        this.date = date;
        this.uidList = uidList;
        this.viewcount = viewcount;
        this.replycount = replycount;
        this.mDownloadURIList = mDownloadURIList;
        this.nickname=nickname;
        this.BoardName=boardName;
    }

    public ArrayList<String> getmDownloadURIList() {
        return mDownloadURIList;
    }

    public void setmDownloadURIList(ArrayList<String> mDownloadURIList) {
        this.mDownloadURIList = mDownloadURIList;
    }

    public int getReplycount() {
        return replycount;
    }

    public void setReplycount(int replycount) {
        this.replycount = replycount;
    }

    public int getViewcount() {
        return viewcount;
    }

    public void setViewcount(int viewcount) {
        this.viewcount = viewcount;
    }

    public BoardInfo(String title, String content, String uid, String documentId, Date date, String deleted_at, List<String> uidList) {
        this.title = title;
        this.content = content;
        this.uid = uid;
        this.documentId = documentId;
        this.deleted_at = deleted_at;
        this.date = date;
        this.uidList = uidList;
    }

    public List<String> getUidList() {
        return uidList;
    }

    public void setUidList(List<String> uidList) {
        this.uidList = uidList;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getDocumentId() {
        return documentId;
    }

    public BoardInfo() {

    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public BoardInfo(String title, String content) {
        this.title = title;
        this.content = content;
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

}
