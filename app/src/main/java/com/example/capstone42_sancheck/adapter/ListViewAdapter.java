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
    private TextView MNTN_NM;
    private TextView PMNTN_NM;
    private TextView PMNTN_LT;
    private TextView PMNTN_TIME;
    private TextView PMNTN_DFFL;
    private TextView PEOPLE;

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

        MNTN_NM = (TextView) view.findViewById(R.id.M_Name);
        PMNTN_NM = (TextView) view.findViewById(R.id.PM_Name);
        PMNTN_LT = (TextView) view.findViewById(R.id.LT);
        PMNTN_TIME = (TextView) view.findViewById(R.id.TIME);
        PMNTN_DFFL = (TextView) view.findViewById(R.id.DFFL);
        PEOPLE = (TextView) view.findViewById(R.id.PEOPLE);

        SearchListViewItem searchListViewItem = arrayList.get(i);

        MNTN_NM.setText(searchListViewItem.getMNTN_NM());
        PMNTN_NM.setText(searchListViewItem.getPMNTN_NM());
        PMNTN_LT.setText(searchListViewItem.getPMNTN_LT().toString() + "km");
        PMNTN_TIME.setText(searchListViewItem.getTime() + "분");
        PMNTN_DFFL.setText(searchListViewItem.getPMNTN_DFFL());
        PEOPLE.setText(searchListViewItem.getPEOPLE() + "명 등산중!");

        return view;
    }

    public void addItem(int index, String MNTN_NM, String PMNTN_NM, Double PMNTN_LT, Double PMNTN_UPPL,
                        Double PMNTN_GODN, String PMNTN_DFFL, String START_PNT, String END_PNT) {

        SearchListViewItem searchListViewItem = new SearchListViewItem();
        searchListViewItem.setIndex(index);
        searchListViewItem.setMNTN_NM(MNTN_NM);
        searchListViewItem.setPMNTN_NM(PMNTN_NM);
        searchListViewItem.setPMNTN_LT(PMNTN_LT);
        searchListViewItem.setPMNTN_UPPL(PMNTN_UPPL);
        searchListViewItem.setPMNTN_GODN(PMNTN_GODN);
        searchListViewItem.setPMNTN_DFFL(PMNTN_DFFL);
        searchListViewItem.setSTART_PNT(START_PNT);
        searchListViewItem.setEND_PNT(END_PNT);

        arrayList.add(searchListViewItem);
    }

    public void clear() {
        arrayList.clear();
    }
}
