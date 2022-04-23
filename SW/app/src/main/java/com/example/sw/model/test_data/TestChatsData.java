package com.example.sw.model.test_data;

import android.widget.ListView;

import com.example.sw.chat.MessageInListModel;
import com.example.sw.chat.MessagesAdapter;
import com.example.sw.model.data_models.MessageData;
import com.example.sw.model.database_interfaces.ChatsDatabaseInterface;

import java.util.ArrayList;
import java.util.HashSet;

public class TestChatsData implements ChatsDatabaseInterface {
    ArrayList<MessageData> messages;

    public TestChatsData() {
        messages = new ArrayList<MessageData>();
        for (int i = 0; i < 30; i++) {
            messages.add(new MessageData(
                    "User1@example.com",
                    "User3@example.com",
                    "some text1"
                    )
            );
            messages.add(new MessageData(
                    "User3@example.com",
                    "User1@example.com",
                    "some text2"
                    )
            );
        }
    }

    @Override
    public ArrayList<String> getInterlocutorsOfUserWithUsername(String username) {
        HashSet<String> interlocutors = new HashSet<String>();

        for (MessageData message: messages) {
            if (message.getSenderUsername().equals(username))
                interlocutors.add(message.getReceiverUsername());
            else if (message.getReceiverUsername().equals(username))
                interlocutors.add(message.getSenderUsername());
        }
        ArrayList<String> interlocutorsList = new ArrayList<String>(interlocutors);

        return interlocutorsList;
    }

    @Override
    public ArrayList<MessageData> getMessagesOfUsers(String sender, String receiver) {
        ArrayList<MessageData> foundMessages = new ArrayList<MessageData>();
        for (MessageData message: messages) {
            if (message.getSenderUsername().equals(sender) && message.getReceiverUsername().equals(receiver))
                foundMessages.add(message);
            else if (message.getReceiverUsername().equals(sender) && message.getSenderUsername().equals(receiver))
                foundMessages.add(message);
        }
        return foundMessages;
    }

    @Override
    public boolean sendMessage(String sender, String receiver, String messageText) {
        int messagesNum = messages.size();
        messages.add(new MessageData(sender, receiver, messageText));

        return true;
    }

    @Override
    public void startListener(ArrayList<MessageInListModel> messages, String sender,
                              MessagesAdapter messagesAdapter, ListView messagesListView) {

    }
}
