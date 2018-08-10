package com.quiz.rtoquiz.Common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.quiz.rtoquiz.Model.Ranking;
import com.quiz.rtoquiz.R;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<Ranking> lstRanking;

    public CustomAdapter(Context context, List<Ranking> lstRanking) {
        this.context = context;
        this.lstRanking = lstRanking;
    }

    @Override
    public int getCount() {
        return lstRanking.size();
    }

    @Override
    public Object getItem(int position) {
        return lstRanking.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.raw,null);

        ImageView imgTop = (ImageView)view.findViewById(R.id.impTop);
        TextView txtTop = (TextView)view.findViewById(R.id.txtTop);

        if(position == 0)
            imgTop.setImageResource(R.drawable.gold);

        else if(position == 1)
            imgTop.setImageResource(R.drawable.silver);
        else
            imgTop.setImageResource(R.drawable.branz);

        txtTop.setText(String.valueOf(lstRanking.get(position).getScore()));
        return view;

            }
}
