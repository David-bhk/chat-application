package com.example.chatf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatf.model.dataLogin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    CircleImageView circleView;
    EditText username, name, password, confirm;
    Button submit;
    private int PERMISSION_GALERI = 101;
    private int ACCESS_GALERI = 201;
    Bitmap bitmap;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dialog = new ProgressDialog(this);
        circleView = findViewById(R.id.circleView);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        confirm = findViewById(R.id.confirm);
        submit = findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successSignUp();

            }
        });

        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_GALERI);
                }else{
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "select image"), ACCESS_GALERI);

                }
            }
        });
    }

    private void successSignUp() {
         String _username = username.getText().toString();
         String _name = name.getText().toString();
         String _password = password.getText().toString();
         String _confirm = confirm.getText().toString();

         if (_username.isEmpty()){
             username.setError("Field can not be empty");
             username.requestFocus();
         }else if (_name.isEmpty()){
             name.setError("Field can not be empty");
             name.requestFocus();
         }else if (_password.isEmpty()){
             password.setError("Field can not be empty");
             password.requestFocus();
         }else if (_confirm.isEmpty()){
             confirm.setError("Field can not be empty");
             confirm.requestFocus();
         }else if (!_confirm.equals(_password)){
             confirm.setError("confirmation must be equal to password");
             confirm.requestFocus();
         }else {
             if (bitmap == null){
                 saveDataToDatabase("");
             }else {
                 saveUploadImage(bitmap);
             }
         }
    }
    private void saveDataToDatabase(String image){
        String _username = username.getText().toString();
        String _name = name.getText().toString();
        String _password = password.getText().toString();

        database.child("login")
                .child(_username)
                .setValue(new dataLogin(_username, _name, _password, image))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        clearEditText();
                        startActivity(new Intent(RegisterActivity.this, Login.class));
                        Toast.makeText(RegisterActivity.this, "data registered successfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                clearEditText();
                Toast.makeText(RegisterActivity.this, "data failed to register", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, Login.class));

            }
        });
    }

    private void saveUploadImage(Bitmap bitmapImage){
        String _username = username.getText().toString();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] dataUpload = byteArrayOutputStream.toByteArray();

        final StorageReference uploadStorage = storage.child(_username);

        uploadStorage.putBytes(dataUpload)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        saveDataToDatabase(uri.toString());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot tasksnapshot) {
                float progress = 100.0f * tasksnapshot.getBytesTransferred() / tasksnapshot.getTotalByteCount();
                dialog.setMessage(String.format("Sign Up Success %.2f", progress) + " %");
                dialog.show();

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_GALERI){
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_GALERI);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACCESS_GALERI && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            try {
                Uri uriGaleri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriGaleri);
                circleView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void clearEditText(){
        username.setText(null);
        name.setText(null);
        password.setText(null);
        confirm.setText(null);
    }
}