package com.example.system.orgchatadmin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class NewCircular extends AppCompatActivity {

    AttachmentAdapter adapter;
    ArrayList<View> list;
    Button post;
    EditText title,description;
    GridView attachment;

    private void create_attachment_view(ArrayList<View> list){

        //View add = getLayoutInflater().inflate(R.layout.attachment_view,null);
        Drawable add = getResources().getDrawable(R.drawable.ic_clip);
        ImageView

        list.add(add);

        adapter.notifyDataSetChanged();

    }

    private Bitmap thumbnailFromPath(Uri uri){

        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(uri.getPath()),
                128,
                128);

        return thumbImage;
    }

    private boolean isVideo(Uri uri){

        String type = getApplication().getContentResolver().getType(uri);

        if(type.startsWith("video"))
            return true;
        else
            return false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_circular);

        post = (Button)findViewById(R.id.post);
        title = (EditText)findViewById(R.id.circular_title);
        description = (EditText)findViewById(R.id.circular_description);
        attachment = (GridView)findViewById(R.id.attachment_list);

        list = new ArrayList<View>();

        adapter = new AttachmentAdapter(NewCircular.this,list);

        attachment.setAdapter(adapter);


    }
}
