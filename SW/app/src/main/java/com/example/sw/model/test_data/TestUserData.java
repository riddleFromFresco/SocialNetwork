package com.example.sw.model.test_data;

import com.example.sw.model.database_interfaces.UserSearchInterface;

import java.util.ArrayList;

public class TestUserData implements UserSearchInterface {
    ArrayList<String> users;

    public TestUserData() {
        users = new ArrayList<String>();

        for (int i = 0; i < 100; i++)
            users.add("User" + String.valueOf(i) + "@example.com");
    }

    @Override
    public ArrayList<String> getUsersBySubstring(String substring) {
        ArrayList<String> match = new ArrayList<String>();

        for (String row: users)
            if (row.contains(substring))
                match.add(row);

        return match;
    }
}
