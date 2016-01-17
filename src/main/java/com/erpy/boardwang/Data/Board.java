package com.erpy.boardwang.Data;

/**
 * Created by oj.bae on 2016. 1. 18..
 */
public class Board {
    private String title;
    private String writer;
    private String url;
    private String thumbUrl;
    private String dateTime;
    private String clickCount;
    private String suggestCount;

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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getClickCount() {
        return clickCount;
    }

    public void setClickCount(String clickCount) {
        this.clickCount = clickCount;
    }

    public String getSuggestCount() {
        return suggestCount;
    }

    public void setSuggestCount(String suggestCount) {
        this.suggestCount = suggestCount;
    }
}
