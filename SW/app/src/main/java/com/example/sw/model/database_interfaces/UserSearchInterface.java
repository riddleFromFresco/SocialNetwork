package com.example.sw.model.database_interfaces;

import java.util.ArrayList;

public interface UserSearchInterface {
    public ArrayList<String> getUsersBySubstring(String substring);
}
