package com.example.semproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    TextView useremail, userpassword;
    Button login;
    ProgressDialog loading;
    private FirebaseAuth mAuth;
    FirebaseUser currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        useremail = findViewById(R.id.email);
        userpassword = findViewById(R.id.password);
        login = findViewById(R.id.loggin);
        loading = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        TextView loginn = findViewById(R.id.signupfromlogin);
        if (mAuth.getCurrentUser() != null) {

            startActivity(new Intent(getApplication(), MainActivity.class));
            finish();
        }
        currentuser = mAuth.getCurrentUser();
        loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(login.this, register.class);
                startActivity(inte);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });

    }

    private void login() {
        loading.setTitle("Sign in");
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();
        String email = useremail.getText().toString().trim();
        String password = userpassword.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(login.this, MainActivity.class);
                    startActivity(intent);
                    loading.dismiss();

                } else {
                    Toast.makeText(login.this, "Login unsuccessful", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        });

    }
}