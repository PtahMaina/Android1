package com.example.semproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class restadapter extends RecyclerView.Adapter<restadapter.restviewholder> {
    Context context;
    ArrayList<User> list;
    Integer OverallPrice=0;
    SharedPreferences sharedPreferences;
    public restadapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public restviewholder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.person,parent,false);

        return new restviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull restadapter.restviewholder holder, int position) {
        User user=list.get(position);
        holder.t1.setText(user.getImageName());
        holder.t2.setText(user.getPrice());
        holder.t3.setText(user.getIngredients());
        OverallPrice=OverallPrice + Integer.valueOf(user.getPrice());
        sharedPreferences=context.getSharedPreferences("savedata",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("value", String.valueOf(OverallPrice));
        editor.apply();

        Picasso.with(context).load(user.getImageURL()).fit().centerCrop().placeholder(R.mipmap.ic_launcher).into(holder.img1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,design.class);
                intent.putExtra("imageurl",user.imageURL);
                intent.putExtra("name",user.imageName);
                intent.putExtra("menu",user.menu);
                intent.putExtra("price",user.price);
                intent.putExtra("grams",user.grams);
                intent.putExtra("ingredients",user.ingredients);
                intent.putExtra("description",user.description);

                context.startActivity(intent);
            }
        });
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public class restviewholder extends RecyclerView.ViewHolder{
        TextView t1,t2,t3;
        ImageView img1,lik;
        public restviewholder(View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.resttitle);
            t2=itemView.findViewById(R.id.det_foodprices);
            img1=itemView.findViewById(R.id.restimage);
            t3=itemView.findViewById(R.id.ingreddd);

        }

    }
}

