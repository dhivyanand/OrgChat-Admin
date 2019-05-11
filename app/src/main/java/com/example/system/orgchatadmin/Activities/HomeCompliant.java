package com.example.system.orgchatadmin.Activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.system.orgchatadmin.Adapters.MessageAdapter;
import com.example.system.orgchatadmin.Constants;
import com.example.system.orgchatadmin.LocalConfig;
import com.example.system.orgchatadmin.Network.APIRequest;
import com.example.system.orgchatadmin.R;
import com.example.system.orgchatadmin.Services.ApplicationBackgroundService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HomeCompliant extends AppCompatActivity {

    public String getUserID(){

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("AppSession", Context.MODE_PRIVATE);

        return sharedpreferences.getString("user","nil");

    }

    public String getUserPassword(){

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("AppSession", Context.MODE_PRIVATE);

        return sharedpreferences.getString("password","nil");

    }

    public String getDeptFromId(String id){

        String name = "";

        try {

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select DEPARTMENT , SUBDEPARTMENT from SUBDEPARTMENT where SUBDEPARTMENT_ID = '"+id+"' ",null);

            if(resultSet.moveToFirst()) {
                name += resultSet.getString(0);
                name += " > ";
                name += resultSet.getString(1);
            }
            resultSet.close();
            mydatabase.close();

        }catch(Exception e){
            System.out.println(e.toString());
        }

        return name;

    }

    public void sync_message(){

        if(!Constants.is_message_syncing){
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute("A");
        }

    }

    public void updateUI(){
        try{

        SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

        Cursor resultSet = mydatabase.rawQuery("Select TITLE, TIME, MESSAGE_ID, SENDER_ID, SUBDEPARTMENT_ID, RW from MESSAGE where MESSAGE_TYPE = 'C' ",null);

        if(resultSet.moveToFirst()) {

            nocomp.setVisibility(View.INVISIBLE);

            do {

                compliant.add(resultSet.getString(0));
                date.add(resultSet.getString(1));
                message_id.add(resultSet.getString(2));
                sender_id.add(resultSet.getString(3));
                subdept_id.add("");
                if(resultSet.getString(5).equals("NR"))
                    status.add("unread");
                else
                    status.add("read");

                adapter.notifyDataSetChanged();

            } while (resultSet.moveToNext());

        }

        resultSet.close();

    }catch (Exception e){

    }}

    ArrayList<String> compliant,date,message_id,sender_id,subdept_id,status;
    MessageAdapter adapter;
    TextView nocomp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_compliant);

        getSupportActionBar().setTitle("Compliant");

        sync_message();

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        nocomp = (TextView)findViewById(R.id.nocomp);
        
        ListView list = (ListView)findViewById(R.id.compliant_list);

        compliant = new ArrayList<String>();
        date = new ArrayList<String>();
        subdept_id = new ArrayList<String>();
        message_id = new ArrayList<String>();
        sender_id = new ArrayList<String>();
        status = new ArrayList<String>();

        adapter = new MessageAdapter(HomeCompliant.this, compliant, date, subdept_id,status);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);

                intent.putExtra("message_id",message_id.get(i));
                intent.putExtra("user_id",sender_id.get(i));
                intent.putExtra("message_type","Compliant");

                SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);
                mydatabase.execSQL("update MESSAGE set RW = 'R' where MESSAGE_ID = '"+ message_id.get(i) +"' ");

                startActivity(intent);
                finish();

            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog dialog = new AlertDialog.Builder(HomeCompliant.this)
                        .setTitle("Delete Item")
                        .setMessage("What do you want to delete this item?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);
                                mydatabase.execSQL("delete from MESSAGE where MESSAGE_ID = '"+ message_id.get(i) +"' ");
                                compliant.remove(i);
                                date.remove(i);
                                subdept_id.remove(i);
                                message_id.remove(i);
                                sender_id.remove(i);

                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return false;
            }
        });

        updateUI();

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... param) {
            try {

                SharedPreferences sharedpreferences = getSharedPreferences("AppSession", Context.MODE_PRIVATE);

                String status = sharedpreferences.getString("status","nil");
                String uname = sharedpreferences.getString("user","nil");
                String pass = sharedpreferences.getString("password","nil");

                SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

                Cursor resultSet = mydatabase.rawQuery("Select LAST_UPDATE, COUNT from DATE where TYPE='Message'",null);

                String res = null;

                if(resultSet.moveToFirst()) {

                    String lastupdate = resultSet.getString(0);
                    String count = resultSet.getString(1);

                    Map<String,String> req = new HashMap<String,String>();
                    req.put("id",uname);
                    req.put("password",pass);
                    req.put("time_stamp",lastupdate);

                    res = APIRequest.processRequest(req, LocalConfig.rootURL+"syncMessages.php",getApplicationContext());

                }else{

                    Map<String,String> req = new HashMap<String,String>();
                    req.put("id",uname);
                    req.put("password",pass);
                    req.put("time_stamp","all");

                    res = APIRequest.processRequest(req, LocalConfig.rootURL+"syncMessages.php",getApplicationContext());

                    mydatabase.execSQL("delete from MESSAGE");

                }


                JSONArray obj = null;

                obj = (JSONArray) new JSONParser().parse(res);

                Iterator<JSONObject> iterator = obj.iterator();
                JSONObject key = null;
                int i=0;


                String id, title, data, sender_id, time=null, attachment_list, type, subdept_id;

                while (iterator.hasNext()) {
                    //iterator.next().toJSONString();
                    JSONObject dept = iterator.next();

                    id = (String)dept.get("id");

                    title = (String)dept.get("title");

                    sender_id = (String)dept.get("sender_id");

                    data = (String)dept.get("data");

                    time = (String)dept.get("time");

                    attachment_list = (String)dept.get("file");

                    type = (String)dept.get("type");

                    subdept_id = (String)dept.get("subdept_id");

                    String attachments[] = attachment_list.split("&");

                    //timebeing
                    mydatabase.execSQL("delete from FILE where MESSAGE_ID = '"+id+"'");

                    for(int c=0 ; c < attachments.length ; c++) {
                        mydatabase.execSQL("insert into FILE values('" + id + "','" + attachments[c] + "','not_available')");
                    }
                    try{
                        mydatabase.execSQL("insert into MESSAGE values('"+id+"','"+sender_id+"','"+title+"','"+data+"','"+type+"','"+time+"','"+subdept_id+"','NR')");
                    }catch(Exception e){}

                    i++;

                }

                mydatabase.execSQL("delete from DATE where TYPE = 'Message'");
                mydatabase.execSQL("insert into DATE values('Message','"+time+"','"+String.valueOf(i)+"')");

                resultSet.close();
                mydatabase.close();

            }catch(Exception e){
                System.out.println("asdfasfadsf"+e.toString());
                e.printStackTrace();
            }
            //updateUI();
            return "A";
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}
