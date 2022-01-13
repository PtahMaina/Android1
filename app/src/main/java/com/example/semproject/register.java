package com.example.semproject;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
    EditText useremail,userpassword,userfullname,userphone;
    Button reg;
    FirebaseAuth mauth;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        reg=findViewById(R.id.register);
        useremail=findViewById(R.id.emailreg);
        userpassword=findViewById(R.id.passwordreg);
        userfullname=findViewById(R.id.fullname);
        userphone=findViewById(R.id.phone);
        loading=new ProgressDialog(this);
        mauth= FirebaseAuth.getInstance();
        TextView register=findViewById(R.id.loginfromregister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte=new Intent(register.this,login.class);
                startActivity(inte);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void signup() {
        loading.setTitle("Sign up");
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();
        final String email=useremail.getText().toString().trim();
        final String password=userpassword.getText().toString().trim();
        final String fullname=userfullname.getText().toString().trim();
        final String phone=userphone.getText().toString().trim();
        mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(getApplication(),MainActivity.class));
                    loading.dismiss();
                }
                else {
                    Toast.makeText(register.this, "unsuccessful", Toast.LENGTH_SHORT).show();
                }
                if (task.isSuccessful()){
                    Users user=new Users(email,fullname,phone);
                    FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(getApplication(),MainActivity.class));
                                loading.dismiss();
                            }
                            else {
                                Toast.makeText(register.this, "unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(register.this, "Sign up unsuccessful", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        });
    }
}