package com.example.system.orgchatadmin.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.system.orgchatadmin.Adapters.AttachmentAdapter;
import com.example.system.orgchatadmin.LocalConfig;
import com.example.system.orgchatadmin.Network.APIRequest;
import com.example.system.orgchatadmin.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewCircular extends AppCompatActivity {

    AttachmentAdapter adapter;
    Button post;
    EditText title,description;
    ImageButton add;
    ListView attachment;
    TextView empty;
    ArrayList<Bitmap> thumbnail;
    ArrayList<Boolean> is_video;
    ArrayList<String> path;
    ArrayList<File> file;

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
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    private void populate_adapter(Bitmap thumb , String uri , Boolean isVideo){

        if(notEmpty(thumbnail, is_video, path)){
            thumbnail.add(thumb);
            is_video.add(isVideo);
            path.add(uri);

            adapter.notifyDataSetChanged();

        }

    }

    private boolean notEmpty(ArrayList<Bitmap> thumbnail, ArrayList<Boolean> is_video, ArrayList<String> path){

        return thumbnail != null && is_video != null && path != null;
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

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private boolean sendToServer(String title, String description, ArrayList<String> uri){

        SharedPreferences sharedpreferences = getSharedPreferences("AppSession", Context.MODE_PRIVATE);

        String user = sharedpreferences.getString("user","nil");
        String password = sharedpreferences.getString("password","nil");

        Map<String,String> arg = new HashMap<String,String>();

        String id = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());

        arg.put("id",user);
        arg.put("password",password);
        arg.put("title",title);
        arg.put("data",description);
        arg.put("circular_id",id);

        for(int i=0;i<uri.size();i++){



        }

        APIRequest.processRequest(arg, LocalConfig.rootURL+"newCircular.php",getApplicationContext());

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
        empty = (TextView)findViewById(R.id.empty);

        thumbnail = new ArrayList<Bitmap>();
        is_video = new ArrayList<Boolean>();
        path = new ArrayList<String>();
        file = new ArrayList<File>();

        adapter = new AttachmentAdapter(NewCircular.this,thumbnail,is_video,path);

        attachment.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                performFileSearch();

            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String t = title.getText().toString();
                String desc = description.getText().toString();

                if(!t.equals("")) {

                    sendToServer(t,desc,path);

                }else{
                    Toast.makeText(NewCircular.this, "Please fill the Title", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                Bitmap b = thumbnailFromPath(uri);
                String path = uri.toString();
                Boolean isVideo = isVideo(uri);

                populate_adapter(b,path,isVideo);

            }
        }
    }


}
