package com.example.semproject;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class intro extends AppCompatActivity {
    ImageView imageView;
    TextView menu,price,tt,ad,ad1,ad2,ad3,bs,bs1,bs2,bs3,sb,sb1,sb2,sb3;
    User users = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tt=findViewById(R.id.total);
        ad=findViewById(R.id.add);
        ad1=findViewById(R.id.add1);
        ad2=findViewById(R.id.add2);
        ad3=findViewById(R.id.add3);
        bs=findViewById(R.id.add_sub);
        bs1=findViewById(R.id.add_sub1);
        bs2=findViewById(R.id.add_sub2);
        bs3=findViewById(R.id.add_sub3);
        sb=findViewById(R.id.sub);
        sb1=findViewById(R.id.sub1);
        sb2=findViewById(R.id.sub2);
        sb3=findViewById(R.id.sub3);
        String tot=getIntent().getStringExtra("pricetocart");
        tt.setText("$"+tot);

    }
}