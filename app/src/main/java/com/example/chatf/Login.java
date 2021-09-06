package com.example.chatf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.chatf.model.dataCommunication;
import com.example.chatf.model.dataLogin;
import com.example.chatf.session.preferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText username, password;
    Button submit, register;
    Switch rememberMe;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        register = findViewById(R.id.register);
        rememberMe = findViewById(R.id.rememberMe);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.isEmpty()){
                    username.setError("Please complete these fields");
                    username.requestFocus();
                }else if (pass.isEmpty()){
                    password.setError("Please complete these fields");
                    password.requestFocus();
                }else {
                    database.child("login").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(user).exists()){
                                dataLogin data = dataSnapshot.child(user).getValue(dataLogin.class);
                                if (data!=null){
                                    if (data.getUsername().equals(user)){
                                        if (data.getPassword().equals(pass)){
                                            if (rememberMe.isChecked()){
                                                startToActivity(true, data);
                                            }else{
                                                startToActivity(false, data);
                                            }

                                        }else{
                                            Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();

                                        }

                                    }else{
                                        Toast.makeText(Login.this, "wrong username", Toast.LENGTH_SHORT).show();

                                    }
                                }

                            }else{
                                Toast.makeText(Login.this, "Username not registered", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, RegisterActivity.class));

            }
        });

    }

    private void startToActivity(boolean active, dataLogin data) {
        preferences.setActiveData(Login.this, active);
        preferences.setKeyData(Login.this, data.getUsername());
        preferences.setNameData(Login.this, data.getName());
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preferences.getActiveData(Login.this)){
            startActivity(new Intent(Login.this, MainActivity.class));
            fileList();
        }
    }
}