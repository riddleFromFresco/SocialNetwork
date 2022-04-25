package com.example.sw.chat_list;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sw.R;

import java.util.ArrayList;

public class ChatsAdapter extends BaseAdapter {
    ArrayList<InterlocutorInListModel> chatList;
    LayoutInflater inflater;

    public ChatsAdapter(Activity context, ArrayList<InterlocutorInListModel> chatList) {
        this.chatList = chatList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public InterlocutorInListModel getItem(int i) {
        return chatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        itemView = (itemView != null) ? itemView : inflater.inflate(R.layout.chat_in_list_widget,null);
        TextView textViewUsername = itemView.findViewById(R.id.chatUsernameTextView);
        InterlocutorInListModel selectedChat = getItem(i);
        textViewUsername.setText(selectedChat.getUserName());

        return itemView;
    }
}
