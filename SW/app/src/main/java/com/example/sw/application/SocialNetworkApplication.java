package com.example.sw.application;

import android.app.Application;

public class SocialNetworkApplication extends Application {
    private String currentUsername;

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }
}
