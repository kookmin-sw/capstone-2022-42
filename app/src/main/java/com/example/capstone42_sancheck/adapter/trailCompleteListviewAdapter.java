package com.example.capstone42_sancheck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.capstone42_sancheck.R;
import com.example.capstone42_sancheck.object.CompleteMountain;

import java.util.ArrayList;

public class trailCompleteListviewAdapter extends BaseAdapter {
//    private Context context = null;
//    private LayoutInflater layoutInflater = null;
    private ArrayList<CompleteMountain> arrayList = new ArrayList<>();

    public trailCompleteListviewAdapter(){

    }

//    public trailCompleteListviewAdapter(Context mContext, ArrayList<CompleteMountain> data){
//        context = mContext;
//        arrayList = data;
//        layoutInflater = LayoutInflater.from(context);
//    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public CompleteMountain getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.frag_home_listview_item, parent, false);
        }
//        View view = layoutInflater.inflate(R.layout.frag_home_listview_item, null);

        TextView tv_monthDay = (TextView) convertView.findViewById(R.id.tv_monthDay);
        TextView tv_year = (TextView) convertView.findViewById(R.id.tv_year);
        TextView tv_mtName = (TextView) convertView.findViewById(R.id.tv_mtName);
        TextView tv_pmtName = (TextView) convertView.findViewById(R.id.tv_pmtName);

        tv_monthDay.setText(arrayList.get(position).getMonthDay());
        tv_year.setText(arrayList.get(position).getYear());
        tv_mtName.setText(arrayList.get(position).getMountainName());
        tv_pmtName.setText(arrayList.get(position).getPmountainName());

        return convertView;
    }

    public void addItem(String monthDay, String year, String mountaionName, String pmountaionName){
        CompleteMountain mountainListViewItem = new CompleteMountain(monthDay, year, mountaionName, pmountaionName);
        arrayList.add(mountainListViewItem);
    }

    public void clear() {
        arrayList.clear();
    }
}
