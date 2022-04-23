package com.example.sw.model.database_interfaces;

import android.widget.ListView;

import com.example.sw.chat.MessageInListModel;
import com.example.sw.chat.MessagesAdapter;
import com.example.sw.model.data_models.MessageData;

import java.util.ArrayList;

public interface ChatsDatabaseInterface {
    public ArrayList<String> getInterlocutorsOfUserWithUsername(String username);
    public ArrayList<MessageData> getMessagesOfUsers(String user1, String user2);
    public boolean sendMessage(String user1, String user2, String messageText);
    public void startListener(ArrayList<MessageInListModel> messages, String sender,
                              MessagesAdapter messagesAdapter, ListView messagesListView);
}
