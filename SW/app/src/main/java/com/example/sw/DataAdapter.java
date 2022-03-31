package com.example.sw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<VeiwHoler> {

    ArrayList<String> messages = new ArrayList<>();
    LayoutInflater layoutInflater;

    public DataAdapter(Context context, ArrayList<String> messages) {
        this.messages = messages;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public VeiwHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_message,parent,false);
        return new VeiwHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VeiwHoler holder, int position) {
        String msg = messages.get(position);
        holder.message.setText(msg);
    }

    @Override
    public int getItemCount() {
        return messages.size() ;
    }
}
