package com.example.system.orgchatadmin.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.system.orgchatadmin.Adapters.AttachmentAdapter;
import com.example.system.orgchatadmin.FileUtil;
import com.example.system.orgchatadmin.LocalConfig;
import com.example.system.orgchatadmin.Network.APIRequest;
import com.example.system.orgchatadmin.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.util.Base64.*;

public class NewCircular extends AppCompatActivity {

    AttachmentAdapter adapter;
    FloatingActionButton post;
    EditText title,description;
    ImageButton add;
    ListView attachment;
    TextView empty;
    ArrayList<Bitmap> thumbnail;
    ArrayList<Boolean> is_video;
    ArrayList<String> path;
    ArrayList<File> file;
    ArrayList<Uri> file_uri;
    RelativeLayout popup;

    int READ_REQUEST_CODE = 42;

    private void performFileSearch(String type) {

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
        intent.setType(type);

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

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Video.Media.DATA, MediaStore.Files.FileColumns.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        if (uri.getHost().contains("com.android.providers.media")) {
            // Image pick from recent
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA,MediaStore.Video.Media.DATA,MediaStore.Files.FileColumns.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(uri,
                    column, null, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } else {
            // image pick from gallery
            return null;// getRealPathFromURI_BelowAPI11(context,uri)
        }

    }

    private boolean notEmpty(ArrayList<Bitmap> thumbnail, ArrayList<Boolean> is_video, ArrayList<String> path){

        return thumbnail != null && is_video != null && path != null;
    }

    private boolean isVideo(Uri uri){

        String type = getApplication().getContentResolver().getType(uri);

        if(type.startsWith("video"))
            return true;
        else
            return false;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean sendToServer(String title, String description, ArrayList<String> uri){

        try {

            SharedPreferences sharedpreferences = getSharedPreferences("AppSession", Context.MODE_PRIVATE);

            String user = sharedpreferences.getString("user", "nil");
            String password = sharedpreferences.getString("password", "nil");

            Map<String, String> arg = new HashMap<String, String>();

            String id = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());

            arg.put("id", user);
            arg.put("password", password);
            arg.put("title", title);
            arg.put("data", description);
            arg.put("circular_id", id);

            Map<String,File> attachment = new HashMap<String,File>();

            for (int i = 0; i < file_uri.size(); i++) {

                String[] filePathColumn = { MediaStore.Images.Media.DATA, MediaStore.Video.Media.DATA, MediaStore.Files.FileColumns.DATA };
                Cursor cursor = getApplication().getContentResolver().query(file_uri.get(i), filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                File file = FileUtil.from(NewCircular.this,file_uri.get(i));// new File(file_uri.get(i).getPath());

                String name = user+id+i+file.getName();

                attachment.put(file.getName(),file);

            }

            String res = APIRequest.processRequest(arg, attachment, LocalConfig.rootURL + "newCircular.php", getApplicationContext());

            JSONObject obj = new JSONObject(res);

            String result = (String)obj.get("result");

            if(result.equals("TRUE")) {

                SQLiteDatabase mydatabase = openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);
                mydatabase.execSQL("insert into CIRCULAR values('"+id+"','"+title+"','"+description+"','"+ new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime())+"','"+  new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) +"') ");

                for (int i = 0; i < file_uri.size(); i++) {

                    String[] filePathColumn = { MediaStore.Images.Media.DATA, MediaStore.Video.Media.DATA, MediaStore.Files.FileColumns.DATA };
                    Cursor cursor = getApplication().getContentResolver().query(file_uri.get(i), filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    File f = FileUtil.from(NewCircular.this,file_uri.get(i));
                    Toast.makeText(this, getRealPathFromURI(file_uri.get(i)), Toast.LENGTH_SHORT).show();

                    mydatabase.execSQL("insert into FILE values('"+id+"','"+f.getName()+"','"+getRealPathFromURI(file_uri.get(i))+"')");

                }

                mydatabase.close();

                return true;
            }else {
                return false;
            }

        }catch(Exception e){
            System.out.println("asdasda"+e.toString());
            return false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_circular);

        post = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        title = (EditText)findViewById(R.id.circular_title);
        description = (EditText)findViewById(R.id.circular_description);
        attachment = (ListView)findViewById(R.id.attachment_list);
        add = (ImageButton)findViewById(R.id.add);
        empty = (TextView)findViewById(R.id.empty);
        popup = (RelativeLayout)findViewById(R.id.popup_layout);

        thumbnail = new ArrayList<Bitmap>();
        is_video = new ArrayList<Boolean>();
        path = new ArrayList<String>();
        file = new ArrayList<File>();
        file_uri = new ArrayList<Uri>();

        adapter = new AttachmentAdapter(NewCircular.this,thumbnail,is_video,path);

        attachment.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        attachment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog dialog = new AlertDialog.Builder(NewCircular.this)
                        .setTitle("Delete Item")
                        .setMessage("What do you want to delete this item?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                thumbnail.remove(i);
                                is_video.remove(i);
                                path.remove(i);

                                file_uri.remove(i);

                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

                return false;
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //performFileSearch();

                LayoutInflater layoutInflater = (LayoutInflater) NewCircular.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.attachment_popup,null);

                LinearLayout attr;
                ImageButton image, video, document, cancel;

                attr = (LinearLayout)customView.findViewById(R.id.linearLayout);

                cancel = (ImageButton)customView.findViewById(R.id.close);

                image = (ImageButton)attr.findViewById(R.id.image);
                video = (ImageButton)attr.findViewById(R.id.video);
                document = (ImageButton)attr.findViewById(R.id.document);

                //closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);

                //instantiate popup window
                final PopupWindow popupWindow = new PopupWindow(customView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

                Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

                popupWindow.setAnimationStyle(R.style.Animation);
                popupWindow.showAtLocation(popup, Gravity.CENTER, 0, 0);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        performFileSearch("image/*");
                        popupWindow.dismiss();
                    }
                });

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        performFileSearch("video/*");
                        popupWindow.dismiss();
                    }
                });

                document.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        performFileSearch("application/*");
                        popupWindow.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                //close the popup window on button click
                /*closePopupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                }); */

            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                String t = title.getText().toString();
                String desc = description.getText().toString();

                if(!t.equals("")) {

                    post.setEnabled(false);

                    if(sendToServer(t,desc,path)){
                        Toast.makeText(NewCircular.this, "Notice sent successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    }

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

                Bitmap b = null;//thumbnailFromPath(uri);
                String path;
                path = uri.getPath();
                file_uri.add(uri);
                Boolean isVideo = isVideo(uri);

                populate_adapter(b,path,isVideo);

            }
        }
    }
}
