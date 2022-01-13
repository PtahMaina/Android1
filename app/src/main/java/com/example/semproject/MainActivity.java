package com.example.semproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private  SearchView searchView;
    private Button btn;
    ImageView txt;
    TextView all;
    ImageView texxtview;
    private SliderAdapter adapter;
    private ArrayList<SliderData> sliderDataArrayList;
    FirebaseFirestore db;
    private SliderView sliderView;
    private RecyclerView recyclerView,recyclerViewpopular;
    DatabaseReference Reference;
    mine mines;
    best bb;
    myadapter adapt;
    TextView textView;
    ArrayList<User> list,lists;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.list);
        recyclerViewpopular = findViewById(R.id.list_popular);
        texxtview=findViewById(R.id.post);
        all=findViewById(R.id.seeall);
        FloatingActionButton floatingActionButton=findViewById(R.id.add_fab);
        sliderDataArrayList = new ArrayList<>();
        // initializing our slider view and
        // firebase firestore instance.
        sliderView = findViewById(R.id.slider);
        db = FirebaseFirestore.getInstance();

        // calling our method to load images.
        loadImages();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte=new Intent(MainActivity.this,post.class);
                startActivity(inte);
            }
        });
        textView=findViewById(R.id.sign);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert();

            }
        });
        texxtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ntent=new Intent(MainActivity.this,cart.class);
                startActivity(ntent);
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents=new Intent(MainActivity.this,trial.class);
                startActivity(intents);

            }
        });

        Reference=FirebaseDatabase.getInstance().getReference("high");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        list =new ArrayList<>();
        bb = new best(this,list);
        recyclerView.setAdapter(bb);

        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User users=dataSnapshot.getValue(User.class);
                    list.add(users);
                }
                bb.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Reference=FirebaseDatabase.getInstance().getReference("allimages");
        GridLayoutManager gridLayoutManagerpopular = new GridLayoutManager(getApplicationContext(),2);
        recyclerViewpopular.setLayoutManager(gridLayoutManagerpopular);
        lists =new ArrayList<>();
        bb = new best(this,lists);
        recyclerViewpopular.setAdapter(bb);

        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User users=dataSnapshot.getValue(User.class);
                    lists.add(users);
                }
                bb.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void alert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to sign out?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(MainActivity.this,login.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent=new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void loadImages() {
        // getting data from our collection and after
        // that calling a method for on success listener.
        db.collection("data").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                // inside the on success method we are running a for loop
                // and we are getting the data from Firebase Firestore
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    // after we get the data we are passing inside our object class.
                    SliderData sliderData = documentSnapshot.toObject(SliderData.class);
                    SliderData model = new SliderData();

                    // below line is use for setting our
                    // image url for our modal class.
                    model.setImgUrl(sliderData.getImgUrl());

                    // after that we are adding that
                    // data inside our array list.
                    sliderDataArrayList.add(model);

                    // after adding data to our array list we are passing
                    // that array list inside our adapter class.
                    adapter = new SliderAdapter(MainActivity.this, sliderDataArrayList);

                    // belows line is for setting adapter
                    // to our slider view
                    sliderView.setSliderAdapter(adapter);
                    // below line is for setting animation to our slider.
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

                    // below line is for setting auto cycle duration.
                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

                    // below line is for setting
                    // scroll time animation
                    sliderView.setScrollTimeInSec(5);

                    // below line is for setting auto
                    // cycle animation to our slider
                    sliderView.setAutoCycle(true);

                    // below line is use to start
                    // the animation of our slider view.
                    sliderView.startAutoCycle();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if we get any error from Firebase we are
                // displaying a toast message for failure
                Toast.makeText(MainActivity.this, "Fail to load slider data..", Toast.LENGTH_SHORT).show();
            }
        });
    }


}