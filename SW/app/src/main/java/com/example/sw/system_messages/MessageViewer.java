package com.example.sw.system_messages;

import android.content.Context;
import android.widget.Toast;

public class MessageViewer {
    public void showMessage(String messageText, Context appContext) {
        Toast message = Toast.makeText(
                appContext,
                messageText,
                Toast.LENGTH_SHORT
        );

        message.show();
    }
}
