package com.example.sw.chat;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sw.R;

import java.util.ArrayList;

public class MessagesAdapter extends BaseAdapter {
    ArrayList<MessageInListModel> messagesList;
    LayoutInflater inflater = null;
    String currentUsername;

    public MessagesAdapter(Activity context, ArrayList<MessageInListModel> chatList, String currentUsername) {
        this.messagesList = chatList;
        this.currentUsername = currentUsername;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messagesList.size();
    }

    @Override
    public MessageInListModel getItem(int i) {
        return messagesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        itemView = (itemView == null) ? inflater.inflate(R.layout.message, null): itemView;
        TextView textViewMessage = (TextView) itemView.findViewById(R.id.messageTextView);
        MessageInListModel currentMessage = getItem(i);
        textViewMessage.setText(currentMessage.getText());

        LinearLayout singleMessageLayout = (LinearLayout)itemView.findViewById(R.id.LinearLayout);
        String messageSender = currentMessage.getSender();

        singleMessageLayout.setGravity(messageSender.equals(currentUsername)? Gravity.RIGHT: Gravity.LEFT);
        textViewMessage.setBackgroundResource(messageSender.equals(currentUsername)? R.drawable.message_mine_bg: R.drawable.message_interlocutor_bg);

        return itemView;
    }
}
