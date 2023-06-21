package com.example.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    CircleImageView circleImageView;
    EditText edt_username, edt_old, edt_phone, edt_location;
    Button btn_save;
    Uri imageUri;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    Intent intent;
    String linkImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        linkImage = "https://i.pinimg.com/736x/c6/e5/65/c6e56503cfdd87da299f72dc416023d4.jpg";
        intent = getIntent();
        circleImageView = findViewById(R.id.img_profile);
        edt_username = findViewById(R.id.edt_username);
        edt_old = findViewById(R.id.edt_old);
        edt_phone = findViewById(R.id.edt_phone);
        edt_location = findViewById(R.id.edt_location);
        btn_save = findViewById(R.id.btn_save);
        progressDialog = new ProgressDialog(this);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Log.d("TAG", "onCreate: " + firebaseAuth.getUid() + firebaseUser.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        storageReference = FirebaseStorage.getInstance().getReference().child("image");
        if (intent != null) {
            linkImage = intent.getStringExtra("imageLink");
            String name = intent.getStringExtra("name");
            edt_username.setText(name);
        }
        Picasso.get()
                .load(linkImage)
                .fit()
                .into(circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });
    }

    private void SaveData() {
        String username = edt_username.getText().toString();
        String old = edt_old.getText().toString();
        String location = edt_location.getText().toString();
        String phone = edt_phone.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            showError(edt_username, "không hợp lệ");
        } else if (old.isEmpty()) {
            showError(edt_old, "không được để trống");
        } else if (location.isEmpty()) {
            showError(edt_location, "không được để trống");
        } else if (phone.isEmpty() || phone.length() <= 9) {
            showError(edt_phone, "không được hợp lệ");
        } else if (linkImage == null) {
            Toast.makeText(this, "Vui lòng chọn hình ảnh", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog.setTitle("add setup profile");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            HashMap hashMap = new HashMap();
            hashMap.put("username", username);
            hashMap.put("old", old);
            hashMap.put("location", location);
            hashMap.put("phone", phone);
            hashMap.put("profileImage", linkImage);
            hashMap.put("status", "offline");

            databaseReference.child(firebaseUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Intent intent = new Intent(Profile.this, MainActivity.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                    Toast.makeText(Profile.this, "Setup profile complete", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Profile.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showError(EditText field, String text) {
        field.setError(text);
        field.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            circleImageView.setImageURI(imageUri);
            storageReference.child(firebaseUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageReference.child(firebaseUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                linkImage = uri.toString();

                            }
                        });
                    }
                }
            });
        }
    }


}