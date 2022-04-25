package com.example.sw.model.firebase_connectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sw.chat.MessageInListModel;
import com.example.sw.chat.MessagesAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class ChatsFirebase {
    final String ACCOUNTS_PATH = "new_messages";
    FirebaseDatabase database;
    DatabaseReference messagesReference;


    public ChatsFirebase() {
        this.database = FirebaseDatabase.getInstance();
        this.messagesReference = database.getReference(ACCOUNTS_PATH);
    }

    public void setMessagesOfUsers(String currentUsername, String interlocutor,
                                   MessagesAdapter messagesAdapter, ArrayList<MessageInListModel> messages)
    {
        messagesReference.orderByChild("sender_receiver").equalTo(
                currentUsername + "$" + interlocutor).addChildEventListener(
                new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // to avoid duplicates in a chat with yourself
                if (currentUsername.equals(interlocutor))
                    return;

                HashMap<String, String> message = (HashMap<String, String>)dataSnapshot.getValue();
                messages.add(new MessageInListModel(message.get("text"), currentUsername, message.get("time")));
                Collections.sort(messages, (m1, m2) -> Long.compare(m1.getTime(), m2.getTime()));

                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        messagesReference.orderByChild("sender_receiver").equalTo(
                interlocutor + "$" + currentUsername).addChildEventListener((
                new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                HashMap<String, String> message = (HashMap<String, String>)dataSnapshot.getValue();
                messages.add(new MessageInListModel(message.get("text"), interlocutor, message.get("time")));
                Collections.sort(messages, (m1, m2) -> Long.compare(m1.getTime(), m2.getTime()));

                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        }));

        }

    public void sendMessage(String sender, String receiver, String messageText) {
        long milliseconds = System.currentTimeMillis();

        HashMap<String, String> newMessage = new HashMap<>();
        newMessage.put("sender_receiver", sender+"$"+receiver);
        newMessage.put("text", messageText);
        newMessage.put("time", String.valueOf(milliseconds));

        messagesReference.push().setValue(newMessage);
    }
}
