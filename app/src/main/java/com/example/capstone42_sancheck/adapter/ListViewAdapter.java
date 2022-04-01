package com.example.capstone42_sancheck.adapter;

import com.example.capstone42_sancheck.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.capstone42_sancheck.object.ListViewItem;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> arrayList = new ArrayList<>();

    public ListViewAdapter() {

    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listviewitem, viewGroup, false);
        }

        TextView text1 = (TextView) view.findViewById(R.id.M_Name);
        TextView text2 = (TextView) view.findViewById(R.id.PM_Name);

        ListViewItem listViewItem = arrayList.get(i);

        text1.setText(listViewItem.getText1());
        text2.setText(listViewItem.getText2());

        return view;
    }

    public void addItem(String text1, String text2) {
        ListViewItem listViewItem = new ListViewItem();
        listViewItem.setText1(text1);
        listViewItem.setText2(text2);

        arrayList.add(listViewItem);
    }

    public void clear() {
        arrayList.clear();
    }
}
