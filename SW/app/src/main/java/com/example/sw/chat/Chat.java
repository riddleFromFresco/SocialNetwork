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
import com.example.sw.model.data_models.MessageData;
import com.example.sw.model.database_interfaces.ChatsDatabaseInterface;
import com.example.sw.model.test_data.TestChatsData;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {
    MessageViewer messageViewer;
    ListView messagesListView;
    MessagesAdapter messagesAdapter;
    RelativeLayout interlocutorLayout;
    EditText messageInput;
    Button sendMessageBtn;
    View interlocutorProfileView;

    ChatsDatabaseInterface testChatsData;
    
    String currentUsername;
    String interlocutorName;

    void setFieldsValues() {
        setViewFields();
        setObjectFields();
        setCurrentUsername();
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
        testChatsData = new TestChatsData();
    }

    void setMessagesListView(ArrayList<MessageInListModel> messagesView) {
        messagesAdapter = new MessagesAdapter(this, messagesView, currentUsername);
        messagesListView.setAdapter(messagesAdapter);
    }

    void setCurrentUsername() {
        currentUsername = ((SocialNetworkApplication) this.getApplication()).getCurrentUsername();
    }

    ArrayList<MessageInListModel> getMessagesView() {
        Bundle messagesBundle = getIntent().getBundleExtra("messages");

        int messagesNum = messagesBundle.size();
        ArrayList<MessageInListModel> messagesView = new ArrayList<MessageInListModel>();

        for (int i = 0; i < messagesNum; i++) {
            MessageData message = (MessageData) messagesBundle.get(String.valueOf(i));
            messagesView.add(new MessageInListModel(message.getMessageText(), message.getSenderUsername()));
        }

        return messagesView;
    }

    void setInterlocutorProfileView(String interlocutorName) {
        TextView textViewUsername = (TextView) interlocutorProfileView.findViewById(R.id.chatUsernameTextView);
        System.out.println(interlocutorName);
        textViewUsername.setText(interlocutorName);
        interlocutorLayout.addView(interlocutorProfileView);
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

    String getInterlocutorName() {
        String interlocutorName = getIntent().getStringExtra("interlocutor");
        return interlocutorName;
    }

    void clearMessageField() {
        messageInput.setText("");
    }

    void sendMessageBtnStartListener() {
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageInput.getText().toString();

                if (isMessageValid(messageText)) {
                    testChatsData.sendMessage(currentUsername, interlocutorName, messageText);
                    clearMessageField();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        setFieldsValues();

        interlocutorName = getInterlocutorName();

        ArrayList<MessageInListModel> messagesView = getMessagesView();
        setMessagesListView(messagesView);

        setInterlocutorProfileView(interlocutorName);

        sendMessageBtnStartListener();

        TestChatsData td = new TestChatsData();
        td.startListener(messagesView, interlocutorName, messagesAdapter, messagesListView);
    }
}
