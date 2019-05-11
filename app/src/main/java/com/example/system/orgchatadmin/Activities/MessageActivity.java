package com.example.system.orgchatadmin.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.system.orgchatadmin.Adapters.AttachmentAdapter;
import com.example.system.orgchatadmin.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MessageActivity extends AppCompatActivity {

    String file_name;

    private String fetchMessageTitle(String id){

        String title = "No Title";

        try {

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select TITLE from MESSAGE where MESSAGE_ID = '"+id+"' ",null);

            if(resultSet.moveToFirst())
                title = resultSet.getString(0);

            resultSet.close();
            mydatabase.close();

        }catch(Exception e){
            System.out.println(e.toString());
        }

        return title;
    }

    private String fetchMessageDescription(String id){

        String desc = "No Title";

        try {

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select MESSAGE from MESSAGE where MESSAGE_ID= '"+id+"' ",null);

            if(resultSet.moveToFirst())
                desc = resultSet.getString(0);

            resultSet.close();
            mydatabase.close();

        }catch(Exception e){
            System.out.println(e.toString());
        }

        return desc;
    }

    private ArrayList<String> populate_attachment_name(String id){

        ArrayList<String> name = new ArrayList<String>();

        try {

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select NAME from FILE where MESSAGE_ID= '"+id+"' ",null);

            if(resultSet.moveToFirst()){

                do{

                    name.add(resultSet.getString(0));

                }while(resultSet.moveToNext());

            }

            resultSet.close();
            mydatabase.close();

        }catch(Exception e){
            System.out.println(e.toString());
        }

        return name;

    }

    private ArrayList<String> populate_attachment_location(String id){

        ArrayList<String> location = new ArrayList<String>();

        try {

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select LOCATION from FILE where MESSAGE_ID= '"+id+"' ",null);

            if(resultSet.moveToFirst()){

                do{

                    location.add(resultSet.getString(0));

                }while(resultSet.moveToNext());

            }

            resultSet.close();
            mydatabase.close();

        }catch(Exception e){
            System.out.println(e.toString());
        }

        return location;

    }

    private boolean getFileFromServer(final String name){

        final Context c = getApplicationContext();

        final ArrayList<String> a = new ArrayList<String>();

        Thread t = new Thread(){

            @Override
            public void run() {

                try {

                    String url = "http://epostbox.sakthiauto.com/syncFile.php";
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    //add reuqest header
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                    String urlParameters = "file_name=" + name;

                    // Send post request
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();

                    DataInputStream din = new DataInputStream(con.getInputStream());

                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + "/OrgChatClient/");
                    f.mkdir();
                    File file = new File(f, name);

                    file_name = file.getAbsolutePath();
                    file_name = Environment.getExternalStorageDirectory() + File.separator + "/OrgChatClient/"+name;

                    file.createNewFile();

                    FileOutputStream fout = new FileOutputStream(file);

                    int inputLine;

                    while ((inputLine = din.read()) != -1) {
                        fout.write(inputLine);
                    }

                    din.close();

                } catch (Exception e) {
                    a.add(e.toString());
                }

            }

        };

        try {
            t.start();
            t.join();
        }catch(Exception e){
            return false;
        }
        return true;



        //print result

    }

    private void setUserTitle(String type, String name, final String userID) {
        // TODO Auto-generated method stub

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();

        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_layout, null);

        TextView user = (TextView)mCustomView.findViewById(R.id.message);
        TextView t = (TextView)mCustomView.findViewById(R.id.type);

        user.setText(name);
        t.setText(type);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),EditUser.class);
                i.putExtra("userID",userID);
                startActivity(i);
            }
        });

        actionbar.setCustomView(mCustomView);
        actionbar.setDisplayShowCustomEnabled(true);

    }

    public String getNameFromId(String id){

        String name = "No Name";

        try {

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select NAME from USER where USER_ID = '"+id+"' ",null);

            if(resultSet.moveToFirst())
                name = resultSet.getString(0);

            resultSet.close();
            mydatabase.close();

        }catch(Exception e){
            System.out.println(e.toString());
        }

        return name;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        TextView title, desc;
        final ListView attachment_list;
        final ArrayList<String> attachment_name, attachment_location;
        AttachmentAdapter adapter;

        title = (TextView)findViewById(R.id.title);
        desc = (TextView)findViewById(R.id.desc);
        attachment_list = (ListView)findViewById(R.id.attachment_list);

        final String message_id = getIntent().getStringExtra("message_id");
        String type = getIntent().getStringExtra("message_type");
        String user_name, user_id;

        user_id = getIntent().getStringExtra("user_id");
        user_name = getNameFromId(user_id);

        setUserTitle(type,user_name,user_id);

        attachment_name = populate_attachment_name(message_id);
        attachment_location = populate_attachment_location(message_id);
        adapter = new AttachmentAdapter(getApplicationContext(),null,null,attachment_name);

        title.setText(fetchMessageTitle(message_id));
        desc.setText(fetchMessageDescription(message_id));

        attachment_list.setAdapter(adapter);

        file_name = null;

        attachment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(attachment_location.get(i).equals("not_available")){
                    Toast.makeText(MessageActivity.this, "Downloading", Toast.LENGTH_SHORT).show();

                    if(getFileFromServer(attachment_name.get(i))){

                        SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);
                        mydatabase.execSQL("update FILE set LOCATION = '"+file_name+"' where MESSAGE_ID = '"+message_id+"'");
                        attachment_location.set(i,file_name);

                    }

                }else{

                    File file = new File(attachment_location.get(i));
                    Toast.makeText(MessageActivity.this, attachment_location.get(i), Toast.LENGTH_SHORT).show();

                    String result = attachment_location.get(i);
                    int cut = result.lastIndexOf(File.separator);
                    result = result.substring(cut + 1);

                    file = new File(Environment.getExternalStorageDirectory()+File.separator+"OrgChatClient/"+result);

                    try {
                        FileOpen.openFile(getApplicationContext(),file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse(attachment_location.get(i));
                    data = Uri.fromFile(file);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.setDataAndType(data, "*/*");

                    //startActivity(intent);

                }

            }
        });

    }
}
