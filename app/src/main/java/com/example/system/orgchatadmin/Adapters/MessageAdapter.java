package com.example.system.orgchatadmin.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.system.orgchatadmin.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {

    Context c;
    ArrayList<String> title,date,department,status;

    public MessageAdapter(Context c, ArrayList<String> title, ArrayList<String> date, ArrayList<String> department, ArrayList<String> status){

        this.c = c;
        this.title = title;
        this.date = date;
        this.department = department;
        this.status = status;

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
        return title.size();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = layoutInflater.inflate(R.layout.mail_list, viewGroup, false);

        TextView content = root.findViewById(R.id.message);
        TextView d = root.findViewById(R.id.time);
        TextView dept = root.findViewById(R.id.dept);

        content.setText(title.get(i));
        d.setText(date.get(i));
        dept.setText(department.get(i));

        if(status.get(i).equals("unread"))
            d.setTextColor(c.getResources().getColor(android.R.color.holo_green_dark));

        return root;
    }
}
