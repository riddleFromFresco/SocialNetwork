package com.example.sw.model.data_models;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageData implements Parcelable {
    String senderUsername;
    String receiverUsername;
    String messageText;


    public MessageData(Parcel in) {
        this.senderUsername = in.readString();
        this.receiverUsername = in.readString();
        this.messageText = in.readString();
    }

    public MessageData(String senderUsername, String receiverUsername, String messageText) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.messageText = messageText;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public static final Creator<MessageData> CREATOR = new Creator<MessageData>() {
        @Override
        public MessageData createFromParcel(Parcel in) {
            return new MessageData(in);
        }

        @Override
        public MessageData[] newArray(int size) {
            return new MessageData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(senderUsername);
        dest.writeString(receiverUsername);
        dest.writeString(messageText);
    }
}
