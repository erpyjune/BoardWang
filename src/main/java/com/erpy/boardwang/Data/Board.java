package com.erpy.boardwang.Data;

/**
 * Created by oj.bae on 2016. 1. 18..
 */
public class Board {
    private int id=0;
    private String title="";
    private String writer="";
    private String url="";
    private String thumbUrl="";
    private String imageUrl="";
    private String dateTime=""; // YYYYmmDD
    private String cpName="";
    private String cpNameDisplay="";
    private int imageCount=0;
    private int videoCount=0;
    private int viewCount=0;
    private int suggestCount=0;
    private int replyCount=0;
    private boolean isDeleted=false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getCpNameDisplay() {
        return cpNameDisplay;
    }

    public void setCpNameDisplay(String cpNameDisplay) {
        this.cpNameDisplay = cpNameDisplay;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getSuggestCount() {
        return suggestCount;
    }

    public void setSuggestCount(int suggestCount) {
        this.suggestCount = suggestCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
