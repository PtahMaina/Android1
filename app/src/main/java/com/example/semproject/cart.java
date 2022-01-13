package com.example.semproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class cart extends AppCompatActivity {
    private RecyclerView recyclerView;
    DatabaseReference Reference;
    cartadapter cart;
    TextView cl;
    ArrayList<Use> list;
    Button check;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=findViewById(R.id.rec);

        Reference= FirebaseDatabase.getInstance().getReference("cart list").child("user view").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child("products");
        cl=findViewById(R.id.clear);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reference.removeValue();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(cart.this,trial.class);
                startActivity(intent);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list =new ArrayList<>();
        check=findViewById(R.id.checkout);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();

            }
        });
        cart = new cartadapter(this,list);
        recyclerView.setAdapter(cart);

        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Use users=dataSnapshot.getValue(Use.class);
                    list.add(users);
                }
                cart.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final Handler handler=new Handler();
        Timer timer=new Timer();
        TimerTask doT=new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            TextView textView=findViewById(R.id.overallprice);
                            SharedPreferences sharedPreference=getSharedPreferences("savdata", Context.MODE_PRIVATE);
                            String value=sharedPreference.getString("value","data not found");
                            textView.setText(value);
                        }
                        catch (Exception e){

                        }

                    }
                });
            }
        };
        timer.schedule(doT,0,1000);

    }

    private void check() {

        Intent intent=new Intent(cart.this,floating.class);
        startActivity(intent);

    }
}