package com.example.capstone42_sancheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.object.Rank;

import java.util.ArrayList;

public class rankAdapter extends RecyclerView.Adapter<rankAdapter.rankViewHolder>
{

    private ArrayList<Rank> arrayList;
    private Context context;

    public rankAdapter(ArrayList<Rank> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public rankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_rank2,parent,false);
        rankViewHolder holder = new rankViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull rankViewHolder holder, int position) {
        holder.userRank.setText(String.valueOf(arrayList.get(position).getRank()));
        holder.userName.setText(arrayList.get(position).getName());
        holder.userScore.setText(String.valueOf(arrayList.get(position).getScore()));

    }

    @Override
    public int getItemCount() {

        return (arrayList!=null ? arrayList.size():0);
    }

    public class rankViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView userScore;
        TextView userRank;

        public rankViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userName =  itemView.findViewById(R.id.userName);
            this.userScore = itemView.findViewById(R.id.userScore);
            this.userRank = itemView.findViewById(R.id.userRank);

        }
    }
}
