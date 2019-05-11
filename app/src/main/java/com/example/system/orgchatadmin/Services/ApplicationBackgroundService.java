package com.example.system.orgchatadmin.Services;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.system.orgchatadmin.Constants;
import com.example.system.orgchatadmin.LocalConfig;
import com.example.system.orgchatadmin.Network.APIRequest;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ApplicationBackgroundService extends Service {

    public ApplicationBackgroundService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void sync_department(String uname, String pass) {

        try {

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Map<String,String> req = new HashMap<String,String>();
            req.put("id",uname);
            req.put("password",pass);
            req.put("user_agent","admin");

            String res = APIRequest.processRequest(req, LocalConfig.rootURL+"fetchAllDept.php",getApplicationContext());

            mydatabase.execSQL("delete from DEPARTMENT");

            JSONArray obj = null;
            try {
                obj = (JSONArray) new JSONParser().parse(res);

                Iterator<JSONObject> iterator = obj.iterator();
                JSONObject key = null;
                int i=0;

                String last_update="0";

                mydatabase.execSQL("delete from DEPARTMENT");
                mydatabase.execSQL("delete from SUBDEPARTMENT");

                while (iterator.hasNext()) {
                    //iterator.next().toJSONString();
                    JSONObject dept = iterator.next();

                    String dept_name = (String)dept.get("name");

                    String dept_id = (String)dept.get("id");

                    last_update = (String)dept.get("last_update");

                    mydatabase.execSQL("insert into DEPARTMENT values ('"+dept_id+"','"+dept_name+"')");

                    System.out.println(dept_id+" "+dept);

                    Map<String,String> subdpt = new HashMap<String,String>();

                    try {
                        JSONArray subdept = (JSONArray) dept.get("subdepartment");

                        System.out.println(dept_name + "\n" + dept_id);

                        Iterator<JSONObject> deptiterator = subdept.iterator();

                        while (deptiterator.hasNext()) {

                            JSONObject object = (JSONObject) deptiterator.next();

                            String subdeptname = (String) object.get("name");
                            String subdeptid = (String) object.get("id");

                            mydatabase.execSQL("insert into SUBDEPARTMENT values ('" + subdeptid + "','" + subdeptname + "','" + dept_name + "')");

                            System.out.println("\t" + subdeptname + "   " + subdeptid);

                        }
                    }catch(Exception e){

                    }

                    i++;

                }

                mydatabase.execSQL("insert into DATE values('Department','"+last_update+"','"+String.valueOf(i)+"')");

                //Toast.makeText(this, last_update+" "+i, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }

            mydatabase.close();

        }catch(SQLException e){

        }

    }

    public void sync_messages(String uname, String pass){

        try {

            SQLiteDatabase mydatabase = context.openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

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
                mydatabase.execSQL("insert into MESSAGE values('"+id+"','"+sender_id+"','"+title+"','"+data+"','"+type+"','"+time+"','"+subdept_id+"','NR')");

                i++;

            }

            mydatabase.execSQL("delete from DATE where TYPE = 'message'");
            mydatabase.execSQL("insert into DATE values('message','"+time+"','"+String.valueOf(i)+"')");

            resultSet.close();
            mydatabase.close();

        }catch(Exception e){
            //Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public boolean check_dept(String uname, String pass){

        try{

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select LAST_UPDATE, COUNT from DATE where TYPE='Department'",null);

            if(resultSet.moveToFirst()) {

                String lastupdate = resultSet.getString(0);
                String count = resultSet.getString(1);

                Map<String,String> req = new HashMap<String,String>();
                req.put("id",uname);
                req.put("password",pass);
                req.put("user_agent","admin");
                req.put("last_update",lastupdate);
                req.put("count",count);

                String res = APIRequest.processRequest(req, LocalConfig.rootURL+"checkDepartmentUpdates.php",getApplicationContext());
                org.json.JSONObject obj = new org.json.JSONObject(res);

                String result = (String)obj.get("result");

                if(result.equals("TRUE"))
                    return true;
                else
                    return false;

            }else{

                Map<String,String> req = new HashMap<String,String>();
                req.put("id",uname);
                req.put("password",pass);
                req.put("user_agent","admin");
                req.put("last_update","0");
                req.put("count","0");

                String res = APIRequest.processRequest(req, LocalConfig.rootURL+"checkDepartmentUpdates.php",getApplicationContext());
                org.json.JSONObject obj = new org.json.JSONObject(res);

                String result = (String)obj.get("result");

                resultSet.close();
                mydatabase.close();

                if(result.equals("TRUE"))
                    return true;
                else
                    return false;

            }

        }catch(Exception e){

        }

        return true;
    }

    public boolean check_user(String uname, String pass){

        try{

            SQLiteDatabase mydatabase = context.openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select LAST_UPDATE, COUNT from DATE where TYPE='User'",null);

            if(resultSet.moveToFirst()) {

                String lastupdate = resultSet.getString(0);
                String count = resultSet.getString(1);

                Map<String,String> req = new HashMap<String,String>();
                req.put("id",uname);
                req.put("password",pass);
                req.put("last_update",lastupdate);
                req.put("count",count);

                String res = APIRequest.processRequest(req, LocalConfig.rootURL+"checkUserUpdates.php",getApplicationContext());
                org.json.JSONObject obj = new org.json.JSONObject(res);

                String result = (String)obj.get("result");

                if(result.equals("TRUE"))
                    return false;
                else
                    return true;

            }else{

                return true;

            }

        }catch(Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }

        return true;
    }

    public void sync_user(String uname, String pass){

        try {

            Constants.is_user_syncing = true;

            //Toast.makeText(this, "Downloading User.", Toast.LENGTH_SHORT).show();
            //Log.i("Downloading","User");

            SQLiteDatabase mydatabase = context.openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            String res = null;

            Map<String,String> req = new HashMap<String,String>();
            req.put("admin_id",uname);
            req.put("admin_password",pass);

            res = APIRequest.processRequest(req, LocalConfig.rootURL+"listUser.php",getApplicationContext());

            JSONArray obj = null;

            obj = (JSONArray) new JSONParser().parse(res);

            Iterator<JSONObject> iterator = obj.iterator();
            JSONObject key = null;
            int i=0;

            String id, subdept_id, name, dob, address, time=null, password, phone, profile;

            mydatabase.execSQL("DELETE FROM USER");

            while (iterator.hasNext()) {
                //iterator.next().toJSONString();
                JSONObject dept = iterator.next();

                id = (String)dept.get("id");

                subdept_id = (String)dept.get("subdept_id");

                name = (String)dept.get("name");

                dob = (String)dept.get("dob");

                time = (String)dept.get("last_update");

                address = (String)dept.get("address");

                password = (String)dept.get("password");

                phone = (String)dept.get("phone");

                profile = (String)dept.get("profile");

                mydatabase.execSQL("insert into USER values('"+id+"','"+subdept_id+"','"+name+"','"+profile+"','"+address+"','"+password+"','"+phone+"')");

                i++;

            }

            mydatabase.execSQL("delete from DATE where TYPE = 'User'");
            mydatabase.execSQL("insert into DATE values('User','"+time+"','"+String.valueOf(i)+"')");

            mydatabase.close();

        }catch(Exception e){
            System.out.println("aksbfkasdkf"+e.toString());
            e.printStackTrace();
        }

        Constants.is_user_syncing = false;

    }

    String uname,pass;
    Context context;

    @Override
    public void onCreate(){

        context = getApplicationContext();

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("A");

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... s) {
            SharedPreferences sharedpreferences = getSharedPreferences("AppSession", Context.MODE_PRIVATE);

            String status = sharedpreferences.getString("status","nil");
            uname = sharedpreferences.getString("user","nil");
            pass = sharedpreferences.getString("password","nil");

            if(status.equals("verified")){

                if(check_user(uname, pass)){

                    sync_user(uname,pass);
                    //sync_messages(uname,pass);

                }

            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            //progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

}
