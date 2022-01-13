package com.example.semproject;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class floating extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating);
        alert();


    }
    private void alert() {
        SharedPreferences sharedPreference=getSharedPreferences("savdata", Context.MODE_PRIVATE);
        String value=sharedPreference.getString("value","data not found");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Pay Sh."+value);
        alertDialogBuilder.setTitle("Payment");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        DatabaseReference Reference= FirebaseDatabase.getInstance().getReference("cart list").child("user view").child(FirebaseAuth.getInstance()
                                .getCurrentUser().getUid()).child("products");
                        Reference.removeValue();
                        Intent intent=new Intent(floating.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}