package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private TextInputEditText edt_username, edt_password, edt_confirm_password;
    Button btn_register, btn_exit;
    TextView tv_login;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        btn_register = findViewById(R.id.btn_register);
        btn_exit = findViewById(R.id.btn_exit);
        tv_login = findViewById(R.id.tv_login);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }

    void SignUp() {
        String email = edt_username.getText().toString();
        String password = edt_password.getText().toString();
        String confirm_password = edt_confirm_password.getText().toString();

        if (email.isEmpty() || !email.contains("@gmail")) {
            showError(edt_username, "không được để trống");
        } else if (password.isEmpty() || password.length() <= 6) {
            showError(edt_password, "Mật khẩu không được để ít hơn 6 kí tự");
        } else if (!confirm_password.equals(password)) {
            showError(edt_confirm_password, "Mật khẩu không trùng lặp");
        } else {
            //       progressDialog.setTitle("Đăng ký thành công");
            progressDialog.setMessage("Vui lòng đợi trong giây lát");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();

                        Intent intent = new Intent(Register.this, Profile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    void showError(TextInputEditText field, String text) {
        field.setError(text);
        field.requestFocus();
    }
}
