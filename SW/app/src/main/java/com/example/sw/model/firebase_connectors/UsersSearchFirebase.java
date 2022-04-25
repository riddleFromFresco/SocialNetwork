package com.example.sw.model.firebase_connectors;

import android.app.Activity;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.sw.chat_list.ChatsAdapter;
import com.example.sw.chat_list.ChatsListMenu;
import com.example.sw.chat_list.InterlocutorInListModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


public class UsersSearchFirebase {
    final String ACCOUNTS_PATH = "accounts";
    final String CHATS_PATH = "new_messages";
    FirebaseDatabase database;
    DatabaseReference accountsReference;
    DatabaseReference chatsReference;

    public UsersSearchFirebase() {
        this.database = FirebaseDatabase.getInstance();
        this.accountsReference = database.getReference(ACCOUNTS_PATH);
        this.chatsReference = database.getReference(CHATS_PATH);
    }

    public void setInterlocutorsOfUserWithUsername(String currentUsername, Activity context) {
        chatsReference.orderByChild("sender_receiver").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashSet<String> interlocutorsNames = new HashSet<>();
                ArrayList<InterlocutorInListModel> usersChats = new ArrayList<>();
                List<HashMap<String, String>> accountsData = getMatchesData(dataSnapshot);

                if (accountsData == null)
                    return;

                for (HashMap<String, String> account: accountsData) {
                    if (currentUsername.equals(Objects.requireNonNull(account.get("sender_receiver")).split("\\$")[0]))
                        interlocutorsNames.add(Objects.requireNonNull(account.get("sender_receiver")).split("\\$")[1]);
                    else if (currentUsername.equals(Objects.requireNonNull(account.get("sender_receiver")).split("\\$")[1]))
                        interlocutorsNames.add(Objects.requireNonNull(account.get("sender_receiver")).split("\\$")[0]);
                }

                for (String interlocutorName: interlocutorsNames) {
                    InterlocutorInListModel currInterlocutor = new InterlocutorInListModel(interlocutorName);
                    if (!usersChats.contains(currInterlocutor))
                        usersChats.add(currInterlocutor);
                }

                ((ChatsListMenu)context).setChatsListView(usersChats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private List<HashMap<String, String>> getMatchesData(DataSnapshot dataSnapshot) {
        Object snapshotValue = dataSnapshot.getValue();
        if (snapshotValue == null)
            return null;

        List<HashMap<String, String>> accountsData = null;

        if (snapshotValue.getClass() == HashMap.class)
            accountsData = (new ArrayList<>(((HashMap<String, HashMap<String, String>>) snapshotValue).values()));
        else if (snapshotValue.getClass() == ArrayList.class)
            accountsData = (ArrayList<HashMap<String, String>>) snapshotValue;

        return accountsData;
    }

    public void setListViewByInterlocutorNameSubstring(String substring, ListView chatsListView, Activity context) {
        accountsReference.orderByChild("username").startAt(substring).endAt(substring+"\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, String>> accountsData = getMatchesData(dataSnapshot);
                if (accountsData == null)
                    return;

                ArrayList<InterlocutorInListModel> chats = new ArrayList<>();

                for (HashMap<String, String> account : accountsData) {
                    if (account != null)
                        chats.add(new InterlocutorInListModel(account.get("username")));
                }
                ChatsAdapter newAdapter = new ChatsAdapter(context, chats);
                chatsListView.setAdapter(newAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}