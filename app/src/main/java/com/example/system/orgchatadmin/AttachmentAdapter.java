package com.example.system.orgchatadmin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.File;
import java.net.FileNameMap;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class AttachmentAdapter extends BaseAdapter {

    Context c;
    ArrayList<Uri> attachment;

    private Bitmap thumbnailFromPath(Uri uri){

        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(uri.getPath()),
                128,
                128);

        return thumbImage;
    }

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

        return null;
    }
}
