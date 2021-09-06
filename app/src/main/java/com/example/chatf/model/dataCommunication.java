package com.example.chatf.model;

public class dataCommunication {
    private String key;

    private String de;
    private String message;
    private String date;
    private long time;
    private String type;


    public dataCommunication() {
    }

    public dataCommunication(String key, String de, String message, String date, long time, String type) {
        this.key = key;
        this.de = de;
        this.message = message;
        this.date = date;
        this.time = time;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

