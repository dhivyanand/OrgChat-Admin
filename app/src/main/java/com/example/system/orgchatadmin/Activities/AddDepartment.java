package com.example.system.orgchatadmin.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
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
import com.example.system.orgchatadmin.Database.CreateDatabaseUsingHelper;
import com.example.system.orgchatadmin.LocalConfig;
import com.example.system.orgchatadmin.Network.APIRequest;
import com.example.system.orgchatadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

        Toast.makeText(this, user+ " "+password, Toast.LENGTH_SHORT).show();

        try {
            String response = APIRequest.processRequest(map,LocalConfig.rootURL+"addDepartment.php",getApplicationContext());

            JSONObject obj = new JSONObject(response);

            String dept_id = (String)obj.get("id");

            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

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
        done = (Button)findViewById(R.id.done);
        dept = (EditText)findViewById(R.id.dept);
        sub_dept = (EditText)findViewById(R.id.sub_dept);
        verify = (ImageButton)findViewById(R.id.verify);
        add = (ImageButton)findViewById(R.id.add_sub_dept);
        list = (ListView)findViewById(R.id.list_user);

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

                if(result == 'S')
                    Toast.makeText(AddDepartment.this, "Department created successfully.", Toast.LENGTH_SHORT).show();
                else if (result == 'F')
                    Toast.makeText(AddDepartment.this, "Failed to create Department", Toast.LENGTH_SHORT).show();
                else if (result == 'I')
                    Toast.makeText(AddDepartment.this, "Unable to reach.", Toast.LENGTH_SHORT).show();


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
