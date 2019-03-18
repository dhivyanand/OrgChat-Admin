package com.example.system.orgchatadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.system.orgchatadmin.R;

import java.util.ArrayList;

public class CircularListAdapter extends BaseAdapter {

    Context c;
    ArrayList<String> title,date;

    public CircularListAdapter(Context c, ArrayList<String> title, ArrayList<String> date){

        this.c = c;
        this.title = title;
        this.date = date;

    }

    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = layoutInflater.inflate(R.layout.grey_list_row, viewGroup, false);

        TextView content = root.findViewById(R.id.dept);
        TextView d = root.findViewById(R.id.attr);

        content.setText(title.get(i));
        d.setText(date.get(i));

        return root;

    }
}
