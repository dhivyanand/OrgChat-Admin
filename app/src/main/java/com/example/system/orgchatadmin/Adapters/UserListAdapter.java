package com.example.system.orgchatadmin.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.system.orgchatadmin.R;

import java.util.ArrayList;

public class UserListAdapter extends BaseAdapter {

    ArrayList<String> name;
    ArrayList<Bitmap> dp;
    Context c;

    public UserListAdapter(Context c, ArrayList<String> name, ArrayList<Bitmap> dp){

        this.c = c;
        this.name = name;
        this.dp = dp;

    }

    @Override
    public int getCount() {
        return name.size();
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
        View root = layoutInflater.inflate(R.layout.user_list_row, viewGroup, false);

        TextView user = root.findViewById(R.id.message);
        ImageView image = root.findViewById(R.id.dp);

        //image.setImageBitmap(dp.get(i));

        user.setText(name.get(i));

        return root;

    }
}
