package com.example.chatf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatf.adapter.CommunicationRecyclerAdapter;
import com.example.chatf.model.dataCommunication;
import com.example.chatf.session.preferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText sendMessage;
    FloatingActionButton fab_send;
    RecyclerView recyclerView;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<dataCommunication> listCommunication = new ArrayList<>();
    ArrayList<String> listData = new ArrayList<>();
    CommunicationRecyclerAdapter communicationRecyclerAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        sendMessage = findViewById(R.id.sendMessage);
        fab_send = findViewById(R.id.fab_send);
        recyclerView = findViewById(R.id.recyclerView);

        fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    inputMessage();
            }
        });

        sendDataMessageShow();

    }

    private void inputMessage() {
        String send = sendMessage.getText().toString();
        if (send.isEmpty()){
            sendMessage.setError("Enter a message");
            sendMessage.requestFocus();

        }else {
            String id = preferences.getKeyData(context);
            String name = preferences.getNameData(context);
            Locale locale = new Locale("en", "ID");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy", locale);

            database.child("message")
                    .push()
                    .setValue(new dataCommunication(
                            id,
                            name,
                            send,
                            simpleDateFormat.format(System.currentTimeMillis()),
                            System.currentTimeMillis(),
                            "text"))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "message sent successfully", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "message failed to send", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    private void sendDataMessageShow() {
        database.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCommunication.clear();
                listData.clear();
                listData.add("");
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    dataCommunication communication = item.getValue(dataCommunication.class);
                    listData.add(communication != null ? communication.getDate(): null);
                    listCommunication.add(communication);
                }
                communicationRecyclerAdapter = new CommunicationRecyclerAdapter(context, listCommunication, listData);
                recyclerView.setAdapter(communicationRecyclerAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}