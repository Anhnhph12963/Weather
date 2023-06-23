package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_profile extends AppCompatActivity {

    CircleImageView circleImageView;
    EditText edt_edit_username, edt_edit_old, edt_edit_phone, edt_edit_location;
    Button btn_edit_save;
    Uri imageUri;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
//
//        intent = getIntent();
//        circleImageView = findViewById(R.id.img_edit_profile);
//        edt_edit_username = findViewById(R.id.edt_edit_username);
//        edt_edit_old = findViewById(R.id.edt_edit_old);
//        edt_edit_phone = findViewById(R.id.edt_edit_phone);
//        edt_edit_location = findViewById(R.id.edt_edit_location);
//        btn_edit_save = findViewById(R.id.btn_edit_save);
//        progressDialog = new ProgressDialog(this);
//
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//        Log.d("TAG", "onCreate: " + firebaseAuth.getUid() + firebaseUser.getUid());
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
//        storageReference = FirebaseStorage.getInstance().getReference().child("image");
    }
}