package com.example.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    ImageView imageView;
    FirebaseAuth firebaseAuth;
    Button btn_login, btn_dk;

    GoogleSignInAccount acct;
    private TextInputEditText edt_username, edt_password;
    TextView tv_forgotpass;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageView = findViewById(R.id.img_google);
        firebaseAuth = FirebaseAuth.getInstance();
        btn_login = findViewById(R.id.btn_login);
        btn_dk = findViewById(R.id.btn_dk);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        tv_forgotpass = findViewById(R.id.tv_forgotpass);
        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        databaseUser = FirebaseDatabase.getInstance().getReference("User");
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


        acct = GoogleSignIn.getLastSignedInAccount(this);

//        String userName = sharedPreferences.getString("username", "");
//        String userPass = sharedPreferences.getString("password", "");
//
//        if (userName != "" && userPass != "") {
//            edt_username.setText(userName);
//            edt_password.setText(userPass);
//        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();

//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("username", edt_username.getText().toString());
//                editor.putString("password", edt_password.getText().toString());
//
//                editor.commit();
            }
        });

        btn_dk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddLogin();
            }
        });
    }

    void SignIn() {
        googleSignInClient.signOut();

        //getting the google signin intent
        Intent signIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, 1000);
//        Toast.makeText(this, "" + acct.getDisplayName()
//                + acct.getGivenName()
//                + acct.getFamilyName()
//                + acct.getEmail()
//                + acct.getId()
//                + acct.getPhotoUrl(), Toast.LENGTH_SHORT).show();
    }

    void AddLogin() {

        String email = edt_username.getText().toString();
        String password = edt_password.getText().toString();

        if (email.isEmpty() || !email.contains("@gmail")) {
            showError(edt_username, "không được để trống");
        } else if (password.isEmpty() || password.length() <= 6) {
            showError(edt_password, "Mật khẩu không được để ít hơn 6 kí tự");
        } else {
            progressDialog.setMessage("Vui lòng đợi trong giây lát");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", edt_username.getText().toString());
                        editor.putString("password", edt_password.getText().toString());
                        editor.commit();

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount googleSignInAccount = task.getResult();
                firebaseAuthWithGoogle(googleSignInAccount);

        }
    }

    void navigateToSencondActivity() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void showError(TextInputEditText field, String text) {
        field.setError(text);
        field.requestFocus();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    Log.d("TAG", "signInWithCredential:success"+firebaseAuth.getUid());
                    DatabaseReference reference =FirebaseDatabase.getInstance().getReference().child("User").child(firebaseAuth.getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
                                Intent intent = new Intent(Login.this, Profile.class);
                                intent.putExtra("imageLink", account.getPhotoUrl());
                                intent.putExtra("name", account.getDisplayName());
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"Tạo Google thành công",Toast.LENGTH_SHORT).show();
                            }else {
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"Đăng nhập thành công ",Toast.LENGTH_SHORT).show();
                            }
//                            Log.d("TAG", "Value is: " + snapshot);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

//                            val user = auth.currentUser
//                            updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.getException());
                    // ...
//                            Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
//                            updateUI(null)
                }
            }
        });
    }
}