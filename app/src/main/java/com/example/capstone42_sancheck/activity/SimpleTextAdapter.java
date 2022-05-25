package com.example.capstone42_sancheck.activity;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone42_sancheck.R;


import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

class ViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public TextView button;

    ViewHolder(Context context, View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.rec_textView);
        button = itemView.findViewById(R.id.rec_button);
    }
}
public class SimpleTextAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<String> arrayList;
    private ArrayList<String> arrayList2;


    public SimpleTextAdapter() {
        arrayList = new ArrayList<>();
        arrayList2 = new ArrayList<>();
    }
//    public SimpleTextAdapter_2() {arrayList_2 = new ArrayList<>();}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recoommender_recycler, parent, false);

        ViewHolder viewholder = new ViewHolder(context, view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = arrayList.get(position);
        holder.textView.setText(text);
        String text2 = arrayList2.get(position);
        holder.button.setText(text2);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // 데이터를 입력
    public void setArrayData(String strData) {

        arrayList.add(strData);
    }

    public void setArrayData2(String strData) {

        arrayList2.add(strData);
    }

}

