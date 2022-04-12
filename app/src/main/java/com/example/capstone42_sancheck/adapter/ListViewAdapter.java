package com.example.capstone42_sancheck.adapter;

import com.example.capstone42_sancheck.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.capstone42_sancheck.object.SearchListViewItem;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<SearchListViewItem> arrayList = new ArrayList<>();

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
            view = inflater.inflate(R.layout.frag_search_listview_item, viewGroup, false);
        }

        TextView text1 = (TextView) view.findViewById(R.id.M_Name);
        TextView text2 = (TextView) view.findViewById(R.id.PM_Name);
        TextView text3 = (TextView) view.findViewById(R.id.LT);

        SearchListViewItem searchListViewItem = arrayList.get(i);

        text1.setText(searchListViewItem.getText1());
        text2.setText(searchListViewItem.getText2());
        text3.setText(searchListViewItem.getText3().toString() + "km");

        return view;
    }

    public void addItem(String text1, String text2, Double text3) {
        SearchListViewItem searchListViewItem = new SearchListViewItem();
        searchListViewItem.setText1(text1);
        searchListViewItem.setText2(text2);
        searchListViewItem.setText3(text3);

        arrayList.add(searchListViewItem);
    }

    public void clear() {
        arrayList.clear();
    }
}
