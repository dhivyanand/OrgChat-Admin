package com.example.system.orgchatadmin;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by System on 5/1/19.
 */

public class MessageAdapter extends BaseAdapter {

    Context c;
    ArrayList<String> message,time;
    ArrayList<Character> direction,type;

    View root;
    TextView text;
    ImageView imgview;

    int screen_height=0, screen_width=0;
    WindowManager wm;
    DisplayMetrics displaymetrics;


    MessageAdapter(Context c , ArrayList<String> message , ArrayList<Character> direction , ArrayList<Character> type , ArrayList<String> time){

        this.c = c;
        this.message = message;
        this.direction = direction;
        this.type = type;
        this.time = time;

    }

    public void img_resize(){
        wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        screen_height = displaymetrics.heightPixels;
        screen_width = displaymetrics.widthPixels;

        imgview.setMinimumWidth(screen_width/2);
        imgview.setMaxWidth(screen_width/2);
        imgview.setMinimumHeight(screen_width/2);
        imgview.setMaxHeight(screen_width/2);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return message.size();
    }

    @Override
    public long getItemId(int i) {
        return message.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        char dir = direction.get(i);
        LayoutInflater layoutInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        char t = type.get(i);

        if(dir == 'R'){

        }else if (dir == 'L'){

        }else if (dir == 'C'){

        }

        return null;
    }
}
