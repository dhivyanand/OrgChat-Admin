package com.example.system.orgchatadmin.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.widget.Toast;

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

            Toast.makeText(this, res, Toast.LENGTH_SHORT).show();

            mydatabase.execSQL("delete from DEPARTMENT");

            JSONArray obj = null;
            try {
                obj = (JSONArray) new JSONParser().parse(res);

                Iterator<JSONObject> iterator = obj.iterator();
                JSONObject key = null;
                int i=0;

                String last_update="0";

                mydatabase.execSQL("delete from DEPARTMENT");

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

                Toast.makeText(this, last_update+" "+i, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }

            mydatabase.close();

        }catch(SQLException e){

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
                req.put("user_agent","user");
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

    @Override
    public void onCreate(){

        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedpreferences = getSharedPreferences("AppSession", Context.MODE_PRIVATE);

        String status = sharedpreferences.getString("status","nil");
        String uname = sharedpreferences.getString("user","nil");
        String pass = sharedpreferences.getString("password","nil");

        if(status.equals("verified")){

            if(check_dept(uname, pass)){

                sync_department(uname, pass);

            }

        }

    }

}
