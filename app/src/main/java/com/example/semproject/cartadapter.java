package com.example.semproject;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class cartadapter extends RecyclerView.Adapter<cartadapter.myviewholders> {
    Context context;
    ArrayList<Use> list;
    DatabaseReference Reference;
    SharedPreferences sharedPreferences;
    Integer OverallPrice=0;

    public cartadapter(Context context, ArrayList<Use> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myviewholders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cartdesign,parent,false);

        return new myviewholders(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  cartadapter.myviewholders holder, int position) {
        Use userr=list.get(position);
        holder.username.setText(userr.getImageName());
        holder.types.setText(userr.getGrams());
        holder.priceinfo.setText("$"+userr.getPrice());
        holder.quantity.setText(userr.getQuantity()+"x");
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        OverallPrice=OverallPrice + Integer.valueOf(userr.getPrice());
        sharedPreferences=context.getSharedPreferences("savdata",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("value", String.valueOf(OverallPrice));
        editor.apply();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myviewholders extends RecyclerView.ViewHolder{
        TextView username,priceinfo,types,quantity,overall;
        ImageView delete;
        public myviewholders(@NonNull  View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.foodname);
            priceinfo=itemView.findViewById(R.id.foodprice);
            types=itemView.findViewById(R.id.type);
            quantity=itemView.findViewById(R.id.quant);
            delete=itemView.findViewById(R.id.del);


        }
    }

}
