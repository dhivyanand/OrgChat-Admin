package com.example.system.orgchatadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.system.orgchatadmin.R;

import java.util.ArrayList;

/**
 * Created by System on 1/4/19.
 */

public class DocListAdapter extends BaseAdapter {

    Context c;
    ArrayList<Integer> images;

    public DocListAdapter(Context c, ArrayList<Integer> images){

        this.c = c;
        this.images = images;

    }

    @Override
    public int getCount() {
        return images.size();
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
        View root = layoutInflater.inflate(R.layout.doc_list_row, viewGroup, false);

        ImageView imageView = (ImageView)root.findViewById(R.id.doc);

        imageView.setImageResource(images.get(i));

        return root;
    }
}
