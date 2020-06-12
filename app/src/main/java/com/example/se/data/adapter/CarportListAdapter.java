package com.example.se.data.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.se.R;

import java.util.ArrayList;

public class CarportListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> info1List, info2List;
    private LayoutInflater inflter;

    public CarportListAdapter(Context applicationContext, ArrayList<String> info1List,
                              ArrayList<String> info2List) {
        this.context = applicationContext;
        this.info1List = info1List;
        this.info2List = info2List;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return info1List.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void updateDistance() {
//        info2List.clear();
//        info2List.addAll(newDistanceInfo);
        this.notifyDataSetChanged();
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_listview, null);
        TextView info1 = view.findViewById(R.id.park_info1);
        TextView info2 = view.findViewById(R.id.park_info2);
        ImageView icon = view.findViewById(R.id.imageView);
        info1.setText(info1List.get(i));
        info2.setText(info2List.get(i));
        icon.setImageResource(R.drawable.port);
        return view;
    }
}
