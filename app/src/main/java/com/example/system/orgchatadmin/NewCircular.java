package com.example.system.orgchatadmin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewCircular extends AppCompatActivity {

    AttachmentAdapter adapter;
    Button post;
    EditText title,description;
    ImageButton add;
    ListView attachment;
    ArrayList<Bitmap> thumbnail;
    ArrayList<Boolean> is_video;
    ArrayList<String> path;

    int READ_REQUEST_CODE = 42;

    private void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    private boolean notEmpty(ArrayList<Bitmap> thumbnail, ArrayList<Boolean> is_video, ArrayList<String> path){

        return thumbnail != null && is_video != null && path != null;
    }

    private Bitmap thumbnailFromPath(Uri uri){

        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(uri.getPath()),
                getSupportActionBar().getHeight(),
                getSupportActionBar().getHeight());

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
        attachment = (ListView)findViewById(R.id.attachment_list);
        add = (ImageButton)findViewById(R.id.add);

        thumbnail = new ArrayList<Bitmap>();
        is_video = new ArrayList<Boolean>();
        path = new ArrayList<String>();

        adapter = new AttachmentAdapter(NewCircular.this,thumbnail,is_video,path);

        attachment.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

    }
}
