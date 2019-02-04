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
    ArrayList<Uri> attachment;

    AttachmentAdapter(Context c, ArrayList<Uri> attachment){

        this.c = c;
        this.attachment = attachment;

    }

    @Override
    public int getCount() {
        return attachment.size();
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

        LayoutInflater inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View root = inflater.inflate(R.layout.attachment_view,null);

        Uri uri = attachment.get(i);

        ImageView link , play;

        link = (ImageView)root.findViewById(R.id.link);
        play = (ImageView)root.findViewById(R.id.play);

        link.setImageBitmap(thumbnailFromPath(uri));

        if(isVideo(uri))
            play.setVisibility(View.VISIBLE);
        else
            play.setVisibility(View.INVISIBLE);

        return root;
    }
}
