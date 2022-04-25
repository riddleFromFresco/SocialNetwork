package com.example.sw.model.firebase_connectors;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.sw.account_operations.LoginMenu;
import com.example.sw.account_operations.SignUpMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccountsFirebase {
    public static final byte CHECK_IF_USERNAME_ALREADY_IN_USE = 0;
    public static final byte CHECK_IF_ACCOUNT_CREATED = 1;

    final String ACCOUNTS_PATH = "accounts";
    FirebaseDatabase database;
    DatabaseReference accountsReference;


    public AccountsFirebase() {
        this.database = FirebaseDatabase.getInstance();
        this.accountsReference = database.getReference(ACCOUNTS_PATH);
    }

    private List<HashMap<String, String>> getMatchesData(DataSnapshot dataSnapshot) {
        Object snapshotValue = dataSnapshot.getValue();
        List<HashMap<String, String>> accountsData = null;

        if (snapshotValue.getClass() == HashMap.class)
            accountsData = (new ArrayList<>(((HashMap<String, HashMap<String, String>>) snapshotValue).values()));
        else if (snapshotValue.getClass() == ArrayList.class)
            accountsData = (ArrayList<HashMap<String, String>>)snapshotValue;

        return accountsData;
    }

    public void getPasswordOfUser(String username, String password, Activity context) {
        accountsReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(
                new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, String>> accountsData = getMatchesData(dataSnapshot);
                if (accountsData != null) {
                    while (accountsData.remove(null));
                    String realPassword = accountsData.get(0).get("password");
                    if (password.equals(realPassword))
                        ((LoginMenu) context).openChatsMenu(username, password, realPassword);
                    else
                        ((LoginMenu) context).wrongPassword();
                }
                else {
                    ((LoginMenu) context).unableToLogin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void checkIfAccountAlreadyExist(String username, String password, Activity context, byte mode) {
        accountsReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(
                new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    if (mode == CHECK_IF_USERNAME_ALREADY_IN_USE)
                        ((SignUpMenu) context).accountAlreadyExistsMessage();
                    else if (mode == CHECK_IF_ACCOUNT_CREATED)
                        ((SignUpMenu) context).openChatsMenu(username);
                }
                else {
                    if (mode == CHECK_IF_USERNAME_ALREADY_IN_USE)
                        ((SignUpMenu) context).createUserWithValidation(username, password);
                    else if (mode == CHECK_IF_ACCOUNT_CREATED)
                        ((SignUpMenu) context).unableToCreateAccountMessage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void createNewAccount(String username, String password) {
        HashMap<String, String> accountsData = new HashMap<>();
        accountsData.put("username", username);
        accountsData.put("password", password);

        accountsReference.push().setValue(accountsData);
    }
}
