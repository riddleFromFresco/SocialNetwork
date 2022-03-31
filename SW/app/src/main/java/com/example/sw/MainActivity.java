package com.example.sw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");

    EditText mEditText;
    Button mButton;
    RecyclerView mRecycler;


    ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.send_message_b);
        mEditText = findViewById(R.id.message_input);
        mRecycler = findViewById(R.id.messages_recycler);


        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        DataAdapter dataAdapter = new DataAdapter(this,messages);

        mRecycler.setAdapter(dataAdapter);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mEditText.getText().toString();
                System.out.println(msg);
                if(msg.equals(""))
                {Toast.makeText(getApplicationContext(),"Введите сообщение!",Toast.LENGTH_SHORT).show();
                    return;}
                if(msg.length()>50)
                {
                    Toast.makeText(getApplicationContext(),"Введите более короткое сообщение!",Toast.LENGTH_SHORT).show();
                    return;

                }
                myRef.push().setValue(msg);
                mEditText.setText("");
            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String msg =snapshot.getValue(String.class);
                messages.add(msg);
                dataAdapter.notifyDataSetChanged();
                mRecycler.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}