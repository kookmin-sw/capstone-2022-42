package com.example.capstone42_sancheck.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.object.Mountain;
import com.example.capstone42_sancheck.object.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_picture;
        private TextView tv_name;
        private TextView tv_level;
        private TextView tv_pname;
        private ImageView iv_deleteheart;
        private FirebaseAuth auth;
        private FirebaseDatabase database;
        private DatabaseReference databaseReference;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_picture = itemView.findViewById(R.id.iv_picture);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_pname = itemView.findViewById(R.id.tv_pname);
            tv_level = itemView.findViewById(R.id.tv_level);
            iv_deleteheart = itemView.findViewById(R.id.deleteheart);

            database = FirebaseDatabase.getInstance("https://capstone42-sancheck-96817-default-rtdb.firebaseio.com/");
            databaseReference = database.getReference("Users");

            iv_deleteheart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    auth = FirebaseAuth.getInstance();
                    String uid = auth.getCurrentUser().getUid();
                    databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue(User.class) != null){
                                User user = snapshot.getValue(User.class);
                                user.setTrailPlanDelete(getPosition(), user.trailPlan);
                                Toast.makeText(itemView.getContext(), "찜 목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                databaseReference.child(uid).setValue(user);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
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
        myViewHolder.tv_name.setText(mountainArrayList.get(position).getMNTN_NM());
        myViewHolder.tv_pname.setText(mountainArrayList.get(position).getPMNTN_NM());
        myViewHolder.tv_level.setText(mountainArrayList.get(position).getPMNTN_DFFL());
    }

    @Override
    public int getItemCount() {
        return mountainArrayList.size();
    }
}
