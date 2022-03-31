package com.example.sw;


import android.view.View;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VeiwHoler extends RecyclerView.ViewHolder {

    TextView message;


    public VeiwHoler(@NonNull View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message_item);
    }
}
