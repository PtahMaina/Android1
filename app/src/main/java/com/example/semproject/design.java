package com.example.semproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class design extends AppCompatActivity {
    ImageView image,bb;
    TextView name,price,desc,ingr,gram,plu,minu,num,fake;
    Button cart;
    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.design);

        image=findViewById(R.id.det_imageurl);
        bb=findViewById(R.id.back1);
        minu=findViewById(R.id.minus);
        plu=findViewById(R.id.plus);
        fake=findViewById(R.id.det_fake_price);
        name=findViewById(R.id.det_foodname);
        price=findViewById(R.id.det_foodprice);
        desc=findViewById(R.id.det_fooddescription);
        ingr=findViewById(R.id.det_foodingredients);
        cart=findViewById(R.id.add_to_cart);
        num=findViewById(R.id.number);
        gram=findViewById(R.id.det_foodgrams);
        mauth= FirebaseAuth.getInstance();
        String gra=getIntent().getStringExtra("grams");
        String fak=getIntent().getStringExtra("price");
        String img=getIntent().getStringExtra("imageurl");
        String nme=getIntent().getStringExtra("name");
        String pric=getIntent().getStringExtra("price");
        String des=getIntent().getStringExtra("description");
        String ing=getIntent().getStringExtra("ingredients");
        gram.setText(gra+"g");
        name.setText(nme);
        price.setText(pric);
        desc.setText(des);
        ingr.setText(ing);
        fake.setText(fak);

        Glide.with(this).asBitmap().load(img).into(image);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtocart();

            }
        });
        plu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number=Integer.parseInt(num.getText().toString().trim());
                number=number+1;
                num.setText(String.valueOf(number));
                int cc=Integer.parseInt(fake.getText().toString().trim());
                int cost=cc*number;
                price.setText(String.valueOf(cost));
            }
        });
        minu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int number=Integer.parseInt(num.getText().toString().trim());
                if (number==0){
                    number=number+0;
                    num.setText(String.valueOf(number));
                }
                else{
                    number=number-1;
                    num.setText(String.valueOf(number));
                    int cc=Integer.parseInt(fake.getText().toString().trim());
                    int cost=cc*number;
                    price.setText(String.valueOf(cost));}

            }
        });

    }

    private void addtocart() {
        String savecurrenttime,savecurrentdate;
        Calendar calfordate=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate=dateFormat.format(calfordate.getTime());
        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime=currenttime.format(calfordate.getTime());
        String productrandomkey=savecurrentdate+savecurrenttime;
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        String productid=name.getText().toString();
        final DatabaseReference cartlistref= FirebaseDatabase.getInstance()
                .getReference().child("cart list");
        final HashMap<String, Object> cartmap=new HashMap<>();
        cartmap.put("pid",productrandomkey);
        cartmap.put("imageName",productid);
        cartmap.put("description",desc.getText().toString());
        cartmap.put("quantity",num.getText().toString());
        cartmap.put("price",price.getText().toString());
        cartmap.put("ingredients",ingr.getText().toString());
        cartmap.put("grams",gram.getText().toString());
        cartmap.put("date",savecurrentdate);
        cartmap.put("time",savecurrenttime);
        cartmap.put("uid",uid);
        cartlistref.child("user view").child(uid).child("products").child(productid).updateChildren(cartmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent inte=new Intent(design.this,cart.class);
                            startActivity(inte);

                        }
                    }
                });




        //Intent bb=new Intent(design.this,intro.class);
        // bb.putExtra("pricetocart",price.getText());
        // startActivity(bb);
    }
}