package com.example.system.orgchatadmin.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.system.orgchatadmin.Adapters.DepartmentListAdapter;
import com.example.system.orgchatadmin.LocalConfig;
import com.example.system.orgchatadmin.Network.APIRequest;
import com.example.system.orgchatadmin.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddDepartment extends AppCompatActivity {

    APIRequest request;
    Button cancel , done;
    EditText dept , sub_dept;
    ImageButton verify , add;
    ListView list;

    ArrayList<String> sub_dept_list;
    DepartmentListAdapter adapter;

    boolean check_sub_department(String sub_department){

        if(!sub_dept_list.contains(sub_department))
            return true;
        else
            return false;
    }

    boolean verify_department(String department){


        return true;
    }

    private char add_to_server() {

        String department = dept.getText().toString();

        final Map<String,String> map = new HashMap<>();
        request = new APIRequest();

        SharedPreferences sharedpreferences = getSharedPreferences("AppSession", Context.MODE_PRIVATE);

        String user = sharedpreferences.getString("user","nil");
        String password = sharedpreferences.getString("password","nil");

        map.put("id",user);
        map.put("password",password);
        map.put("new_dept",department);

        try {
            String response = APIRequest.processRequest(map,LocalConfig.rootURL+"addDepartment.php",getApplicationContext());

            JSONObject obj = new JSONObject(response);

            String dept_id = (String)obj.get("id");
            String time = (String)obj.get("time");
            String result = (String)obj.get("result");

            if(result.equals("TRUE")) {

                SQLiteDatabase mydatabase = openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

                mydatabase.execSQL("insert into DEPARTMENT values('" + dept_id + "','" + department + "')");

                int numRows = (int) DatabaseUtils.queryNumEntries(mydatabase, "DEPARTMENT");

                mydatabase.execSQL("delete from DATE where TYPE = 'DEPARTMENT'");
                mydatabase.execSQL("insert into DATE values('DEPARTMENT','" + time + "','" + numRows + "')");


                for( int i=0 ; i < sub_dept_list.size() ; i++) {

                    Map map1 = new HashMap<String, String>();
                    map1.put("dept",department);
                    map1.put("sub_dept",sub_dept_list.get(i));
                    map1.put("id",user);
                    map1.put("password",password);

                    response = APIRequest.processRequest(map1, LocalConfig.rootURL + "addSubDepartment.php", getApplicationContext());

                    obj = new JSONObject(response);

                    result = (String)obj.get("result");

                    if(result.equals("TRUE")) {

                        String sub_dept_id = (String)obj.get("id");
                        String sub_dept_time = (String)obj.get("time");

                        mydatabase.execSQL("insert into SUBDEPARTMENT values('" + sub_dept_id + "','" + sub_dept_list.get(i) + "','" + department + "')");

                        numRows = (int) DatabaseUtils.queryNumEntries(mydatabase, "SUBDEPARTMENT");

                        mydatabase.execSQL("delete from DATE where TYPE = 'SUBDEPARTMENT'");
                        mydatabase.execSQL("insert into DATE values('SUBDEPARTMENT','" + sub_dept_time + "','" + numRows + "')");

                    }

                }

                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

            } else {

                return 'F';

            }

        } catch (Exception e) {
            return 'F';
        }

        // 'S' success
        // 'F' Failed
        // 'I' No internet

        return 'S';
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department);

        cancel = (Button)findViewById(R.id.cancel);
        done = (Button)findViewById(R.id.save);
        dept = (EditText)findViewById(R.id.message);
        sub_dept = (EditText)findViewById(R.id.sub_dept);
        verify = (ImageButton)findViewById(R.id.verify);
        add = (ImageButton)findViewById(R.id.add_sub_dept);
        list = (ListView)findViewById(R.id.list_questions);

        sub_dept_list = new ArrayList<String>();
        adapter = new DepartmentListAdapter(AddDepartment.this, sub_dept_list);
        list.setAdapter(adapter);

        sub_dept.setEnabled(false);
        add.setEnabled(false);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                char result = add_to_server();

                if(result == 'S') {
                    Toast.makeText(AddDepartment.this, "Department created successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                }else if (result == 'F') {
                    Toast.makeText(AddDepartment.this, "Failed to create Department", Toast.LENGTH_SHORT).show();
                }else if (result == 'I') {
                    Toast.makeText(AddDepartment.this, "Unable to reach.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String department = dept.getText().toString();

                if(verify_department(department)){

                    sub_dept.setEnabled(true);
                    add.setEnabled(true);
                    dept.setEnabled(false);

                }else{
                    Toast.makeText(AddDepartment.this, "Department not satisfied.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sub_department = sub_dept.getText().toString();

                if (check_sub_department(sub_department)) {

                        sub_dept_list.add(sub_department);
                        adapter.notifyDataSetChanged();
                        sub_dept.setText("");

                } else {
                    Toast.makeText(AddDepartment.this, "Already available.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
