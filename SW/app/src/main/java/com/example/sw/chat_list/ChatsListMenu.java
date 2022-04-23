package com.example.sw.chat_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sw.R;
import com.example.sw.account_operations.MessageViewer;
import com.example.sw.account_operations.Validators;
import com.example.sw.application.SocialNetworkApplication;
import com.example.sw.chat.Chat;
import com.example.sw.model.data_models.MessageData;
import com.example.sw.model.database_interfaces.ChatsDatabaseInterface;
import com.example.sw.model.database_interfaces.UserSearchInterface;
import com.example.sw.model.test_data.TestChatsData;
import com.example.sw.model.test_data.TestUserData;

import java.util.ArrayList;

public class ChatsListMenu extends AppCompatActivity {
    String currentUsername;

    SearchView findUserField;
    ListView chatsListView;
    ChatsAdapter chatsAdapter;
    Validators validators;
    MessageViewer messageViewer;

    UserSearchInterface testUserData;
    ChatsDatabaseInterface testChatsData;

    void openChat(String interlocutor) {
        ArrayList<MessageData> messages = testChatsData.getMessagesOfUsers(currentUsername, interlocutor);

        Intent intent = new Intent(ChatsListMenu.this, Chat.class);
        Bundle messagesBundle = new Bundle();
        Bundle interlocutorNameBundle = new Bundle();
        int i = 0;
        for (MessageData message: messages) {
            messagesBundle.putParcelable(String.valueOf(i), message);
            i++;
        }
        intent.putExtra("messages", messagesBundle);
        intent.putExtra("interlocutor", interlocutor);
        startActivity(intent);
    }

    ArrayList<InterlocutorInListModel> findUserInDatabase(String userName) {
        ArrayList<String> users = testUserData.getUsersBySubstring(userName);
        ArrayList<InterlocutorInListModel> foundChats = new ArrayList<InterlocutorInListModel>();
        for (String row: users)
            if (row.contains(userName))
                foundChats.add(new InterlocutorInListModel(row));

        return foundChats;
    }

    void showFoundUsers(ArrayList<InterlocutorInListModel> chats) {
        ChatsAdapter newAdapter = new ChatsAdapter(this, chats);
        chatsListView.setAdapter(newAdapter);
    }

    void setFieldsValues() {
        setViewFields();
        setObjectFields();
    }

    void setViewFields() {
        findUserField = findViewById(R.id.findUserField);
        chatsListView = findViewById(R.id.chatsListView);
        messageViewer = new MessageViewer();
    }

    void setObjectFields() {
        testUserData = new TestUserData();
        testChatsData = new TestChatsData();
    }

    void setCurrentUsername() {
        SocialNetworkApplication application = (SocialNetworkApplication) this.getApplication();
        currentUsername = application.getCurrentUsername();
        System.out.println(currentUsername);
    }

    void setChatsListView() {
        chatsListView.setAdapter(chatsAdapter);
    }

    boolean isDataFound(String query) {
        ArrayList<InterlocutorInListModel> foundData = findUserInDatabase(query);
        return (foundData.size() == 0);
    }

    boolean findButtonPressed(String query) {
        if (!validators.isValidEmail(query)) {
            messageViewer.showMessage("Некорректное имя пользователя.", getApplicationContext());
            setChatsListView();
            return false;
        }

        if (!isDataFound(query)) {
            messageViewer.showMessage("Пользователь не найден", getApplicationContext());
            setChatsListView();
            return false;
        }
        return true;
    }

    boolean queryChanged(String query) {
        if (query.length() > 0) {
            ArrayList<InterlocutorInListModel> foundInterlocutors = findUserInDatabase(query);

            if (foundInterlocutors != null) {
                chatsListView.setAdapter(null);
                showFoundUsers(foundInterlocutors);
            }
        }
        else
            chatsListView.setAdapter(chatsAdapter);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_list);

        setCurrentUsername();

        setFieldsValues();

        ArrayList<String> chatsList = testChatsData.getInterlocutorsOfUserWithUsername(currentUsername);
        ArrayList<InterlocutorInListModel> chats = new ArrayList<InterlocutorInListModel>();
        for (String interlocutor: chatsList)
            chats.add(new InterlocutorInListModel(interlocutor));

        chatsAdapter = new ChatsAdapter(this, chats);

        setChatsListView();

        chatsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String username = (((InterlocutorInListModel)(chatsListView.getAdapter().getItem(i))).getUserName());
                openChat(username);
            }
        });

        findUserField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return findButtonPressed(query);
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return queryChanged(query);
            }
        });
    }

}
