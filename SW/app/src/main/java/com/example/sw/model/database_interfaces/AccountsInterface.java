package com.example.sw.model.database_interfaces;

public interface AccountsInterface {
    String getPasswordOfUser(String username);
    boolean doesUserExist(String username);
    boolean createNewAccount(String username, String password);
}
