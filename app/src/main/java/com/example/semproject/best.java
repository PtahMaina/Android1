package com.example.semproject;

import android.content.Context;
import android.content.Intent;
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

public class best extends RecyclerView.Adapter<best.restviewholder> {
    Context context;
    ArrayList<User> list;

    public best(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public restviewholder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.btw,parent,false);

        return new restviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull best.restviewholder holder, int position) {
        User user=list.get(position);
        holder.t1.setText(user.getImageName());
        holder.t2.setText(user.getPrice());
        holder.t3.setText(user.getMenu());


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
            t1=itemView.findViewById(R.id.name);
            t2=itemView.findViewById(R.id.price);
            img1=itemView.findViewById(R.id.imagedesc);
            t3=itemView.findViewById(R.id.menu);

        }
    }
}
