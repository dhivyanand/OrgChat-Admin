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
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class CircularActivity extends AppCompatActivity {

    String file_name;

    private String fetchCircularTitle(String id){

        String title = "No Title";

        try {

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select TITLE from CIRCULAR where CIRCULAR_ID = '"+id+"' ",null);

            if(resultSet.moveToFirst())
                title = resultSet.getString(0);

            resultSet.close();
            mydatabase.close();

        }catch(Exception e){
            System.out.println(e.toString());
        }

        return title;
    }

    private String fetchCircularDescription(String id){

        String desc = "No Title";

        try {

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select MESSAGE from CIRCULAR where CIRCULAR_ID= '"+id+"' ",null);

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

                    String url = "https://ide50-dhivianand998.legacy.cs50.io:8080/Org_chat_Server/scripts/syncFile.php";
                    URL obj = new URL(url);
                    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

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

                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + "/OrgChatClient");
                    f.mkdir();
                    File file = new File(f, name);

                    file_name = file.getPath();

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
            if(a.size()>0)
                Toast.makeText(c, a.get(0), Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;



        //print result

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular);

        TextView title, desc;
        final ListView attachment_list;
        final ArrayList<String> attachment_name, attachment_location;
        AttachmentAdapter adapter;

        title = (TextView)findViewById(R.id.title);
        desc = (TextView)findViewById(R.id.desc);
        attachment_list = (ListView)findViewById(R.id.attachment_list);

        final String circular_id = getIntent().getStringExtra("circular_id");

        attachment_name = populate_attachment_name(circular_id);
        attachment_location = populate_attachment_location(circular_id);
        adapter = new AttachmentAdapter(getApplicationContext(),null,null,attachment_name);

        title.setText(fetchCircularTitle(circular_id));
        desc.setText(fetchCircularDescription(circular_id));

        attachment_list.setAdapter(adapter);

        file_name = null;

        attachment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(attachment_location.get(i).equals("not_available")){
                    Toast.makeText(CircularActivity.this, "Downloading", Toast.LENGTH_SHORT).show();

                    if(getFileFromServer(attachment_name.get(i))){

                        SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);
                        mydatabase.execSQL("update FILE set LOCATION = '"+file_name+"' where MESSAGE_ID = '"+circular_id+"'");
                        attachment_location.set(i,file_name);

                    }

                }else{

                    Toast.makeText(CircularActivity.this, attachment_location.get(i), Toast.LENGTH_SHORT).show();

                    File file = new File(attachment_location.get(i));

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse(attachment_location.get(i));
                    data = Uri.fromFile(file);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.setDataAndType(data, "*/*");

                    startActivity(intent);

                }

            }
        });

    }
}
