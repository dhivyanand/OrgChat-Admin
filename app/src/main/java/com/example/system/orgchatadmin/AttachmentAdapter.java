package com.example.system.orgchatadmin;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.net.FileNameMap;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class AttachmentAdapter extends BaseAdapter {

    Context c;
    ArrayList<Bitmap> thumbnail;
    ArrayList<Boolean> is_video;
    ArrayList<String> path;
    View root;

    AttachmentAdapter(Context c, ArrayList<Bitmap> thumbnail, ArrayList<Boolean> is_video, ArrayList<String> path){

        this.c = c;
        this.thumbnail = thumbnail;
        this.is_video = is_video;
        this.path = path;

    }

    @Override
    public int getCount() {
        return path.size();
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

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = inflater.inflate(R.layout.attachment_view,null);

        return view;
    }
}
