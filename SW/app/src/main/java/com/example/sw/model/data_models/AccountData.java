package com.example.sw.model.data_models;

public class AccountData {
    String username;
    String password;

    public AccountData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AccountData() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
