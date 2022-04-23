package com.example.sw.chat;

public class MessageInListModel {
    private String text;
    private String sender;

    public MessageInListModel(String text, String sender) {
        this.text = text;
        this.sender = sender;
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
}
