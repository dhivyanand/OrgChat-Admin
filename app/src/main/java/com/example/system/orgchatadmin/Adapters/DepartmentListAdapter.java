package com.example.system.orgchatadmin.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DepartmentListAdapter extends BaseAdapter {

    Context c;
    Map<String,String> content;

    public DepartmentListAdapter(Context c, Map<String, String> content){

        this.c = c;
        this.content = content;

    }

    public Object getElementByIndex(HashMap map, int index){
        return map.get( (map.keySet().toArray())[ index ] );
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
        return null;
    }
}
