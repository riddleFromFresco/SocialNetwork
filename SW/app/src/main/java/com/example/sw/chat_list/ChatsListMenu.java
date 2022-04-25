package com.example.sw.chat_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sw.R;
import com.example.sw.account_operations.MessageViewer;
import com.example.sw.application.SocialNetworkApplication;
import com.example.sw.chat.Chat;
import com.example.sw.model.firebase_connectors.ChatsFirebase;
import com.example.sw.model.firebase_connectors.UsersSearchFirebase;

import java.util.ArrayList;

public class ChatsListMenu extends AppCompatActivity {
    String currentUsername;

    SearchView findUserField;
    ListView chatsListView;
    ChatsAdapter chatsAdapter;
    MessageViewer messageViewer;

    ChatsFirebase chatsFirebase;
    UsersSearchFirebase usersSearchFirebase;


    public void openChat(String interlocutor) {
        Intent intent = new Intent(ChatsListMenu.this, Chat.class);
        intent.putExtra("interlocutor", interlocutor);

        startActivity(intent);
    }

    void setFieldsValues() {
        setCurrentUsername();
        setViewFields();
        setObjectFields();
        setAllInterlocutorsOfCurrentUser();

        this.setTitle(currentUsername);
    }

    void setCurrentUsername() {
        SocialNetworkApplication application = (SocialNetworkApplication) this.getApplication();
        currentUsername = application.getCurrentUsername();
    }

    void setViewFields() {
        findUserField = findViewById(R.id.findUserField);
        chatsListView = findViewById(R.id.chatsListView);
        messageViewer = new MessageViewer();
    }

    void setObjectFields() {
        chatsFirebase = new ChatsFirebase();
        usersSearchFirebase = new UsersSearchFirebase();
    }

    void setAllInterlocutorsOfCurrentUser() {
        usersSearchFirebase.setInterlocutorsOfUserWithUsername(currentUsername, this);
    }

    public void setChatsListView(ArrayList<InterlocutorInListModel> usersChats) {
        ChatsAdapter newAdapter = new ChatsAdapter(this, usersChats);
        chatsListView.setAdapter(newAdapter);
    }

    boolean queryChanged(String query) {
        if (query.length() > 0) {
            chatsListView.setAdapter(null);
            usersSearchFirebase.setListViewByInterlocutorNameSubstring(query, chatsListView, this);
        }
        else {
            chatsListView.setAdapter(chatsAdapter);
            usersSearchFirebase.setInterlocutorsOfUserWithUsername(currentUsername, this);
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_list);

        setFieldsValues();

        chatsListView.setOnItemClickListener((adapterView, view, i, l) -> {
            String interlocutor = (((InterlocutorInListModel)(chatsListView.getAdapter().getItem(i))).getUserName());
            openChat(interlocutor);
        });

        findUserField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return true;}

            @Override
            public boolean onQueryTextChange(String query) {
                return queryChanged(query);
            }
        });
    }
}
