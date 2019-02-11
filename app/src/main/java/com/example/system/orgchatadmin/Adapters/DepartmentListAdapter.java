package com.example.system.orgchatadmin.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.system.orgchatadmin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class DepartmentListAdapter extends BaseAdapter {

    Context c;
    ArrayList<String> content;

    public DepartmentListAdapter(Context c, ArrayList<String> content){

        this.c = c;
        this.content = content;

    }

    @Override
    public int getCount() {
        return content.size();
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
        View root = layoutInflater.inflate(R.layout.department_list_row, viewGroup, false);

        TextView subDepartment = root.findViewById(R.id.dept);

        subDepartment.setText(content.get(i));

        return root;
    }
}
