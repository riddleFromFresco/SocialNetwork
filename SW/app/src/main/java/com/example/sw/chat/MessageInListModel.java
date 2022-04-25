package com.example.sw.chat;

public class MessageInListModel {
    private String text;
    private String sender;
    private long time;

    public MessageInListModel(String text, String sender, String time) {
        this.text = text;
        this.sender = sender;
        this.time = Long.valueOf(time);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
