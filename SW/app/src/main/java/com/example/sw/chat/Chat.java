package com.example.sw.chat;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sw.R;
import com.example.sw.account_operations.MessageViewer;
import com.example.sw.application.SocialNetworkApplication;
import com.example.sw.model.firebase_connectors.ChatsFirebase;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {
    MessageViewer messageViewer;
    ListView messagesListView;
    ArrayList<MessageInListModel> messagesView;
    MessagesAdapter messagesAdapter;
    RelativeLayout interlocutorLayout;
    EditText messageInput;
    Button sendMessageBtn;
    View interlocutorProfileView;

    String currentUsername;
    String interlocutorName;

    ChatsFirebase chatsFirebase;


    void setFieldsValues() {
        setViewFields();
        setObjectFields();
        setCurrentUsername();
        setInterlocutorName();
        setMessagesListView();

        this.setTitle(currentUsername);
    }

    void setViewFields() {
        sendMessageBtn = findViewById(R.id.sendMessageBtn);
        messageInput = findViewById(R.id.messageInput);
        messagesListView = findViewById(R.id.messageListView);
        interlocutorLayout = findViewById(R.id.interlocutorLayout);

        interlocutorProfileView = (interlocutorProfileView == null) ? getLayoutInflater().inflate(R.layout.chat_in_list_widget, null):
                                    getLayoutInflater().inflate(R.layout.chat_in_list_widget, null);
    }

    void setObjectFields() {
        messageViewer = new MessageViewer();
        chatsFirebase = new ChatsFirebase();
    }

    void setCurrentUsername() {
        currentUsername = ((SocialNetworkApplication) this.getApplication()).getCurrentUsername();
    }

    void setInterlocutorName() {
        String interlocutorName = getIntent().getStringExtra("interlocutor");
        this.interlocutorName = interlocutorName;
    }

    void setMessagesListView() {
        messagesView = new ArrayList<MessageInListModel>();
        messagesAdapter = new MessagesAdapter(this, messagesView, currentUsername);
        messagesListView.setAdapter(messagesAdapter);
    }

    void setInterlocutorProfileView(String interlocutorName) {
        TextView textViewUsername = (TextView) interlocutorProfileView.findViewById(R.id.chatUsernameTextView);
        textViewUsername.setText(interlocutorName);
        interlocutorLayout.addView(interlocutorProfileView);
    }

    void sendMessageBtnStartListener() {
        sendMessageBtn.setOnClickListener(view -> {
            String messageText = messageInput.getText().toString();

            if (isMessageValid(messageText)) {
                chatsFirebase.sendMessage(currentUsername, interlocutorName, messageText);
                clearMessageField();
            }
        });
    }

    boolean isMessageValid(String messageText) {
        if (messageText.equals("")) {
            messageViewer.showMessage("Введите сообщение!", getApplicationContext());
            return false;
        }
        if (messageText.length() > 50) {
            messageViewer.showMessage("Введите более короткое сообщение!", getApplicationContext());
            return false;
        }
        return true;
    }

    void clearMessageField() {
        messageInput.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        setFieldsValues();

        setInterlocutorProfileView(interlocutorName);

        sendMessageBtnStartListener();

        chatsFirebase.setMessagesOfUsers(currentUsername, interlocutorName, messagesAdapter, messagesView);
    }
}
