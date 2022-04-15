package com.example.capstone42_sancheck.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.object.Mountain;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_picture;
        private TextView tv_name;
        private TextView tv_level;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_picture = itemView.findViewById(R.id.iv_picture);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_level = itemView.findViewById(R.id.tv_level);
        }
    }

    private ArrayList<Mountain> mountainArrayList;
    public RecyclerViewAdapter(ArrayList<Mountain> mountainArrayList){
        this.mountainArrayList = mountainArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cart_recycleriew_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.iv_picture.setImageResource(mountainArrayList.get(position).getDrawableId());
        myViewHolder.tv_name.setText(mountainArrayList.get(position).getM_Name());
        myViewHolder.tv_level.setText(mountainArrayList.get(position).getDiff());
    }

    @Override
    public int getItemCount() {
        return mountainArrayList.size();
    }
}
