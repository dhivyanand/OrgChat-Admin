package com.example.system.orgchatadmin.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.system.orgchatadmin.LocalConfig;
import com.example.system.orgchatadmin.Network.APIRequest;
import com.example.system.orgchatadmin.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddUser extends AppCompatActivity {

    String first_dept;
    Spinner dept, subdept;

    ArrayList<String> fetch_local_department(){

        ArrayList<String> list = new ArrayList<String>();

        try {

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select * from DEPARTMENT",null);

            if(resultSet.moveToFirst()) {

                do {

                    String dept = resultSet.getString(1);
                    list.add(dept);

                } while (resultSet.moveToNext());

                first_dept = list.get(0);

            }


            resultSet.close();
            mydatabase.close();

        }catch(SQLException e){

        }

        return list;

    }

    ArrayList<String> fetch_local_subdepartment(String department){

        ArrayList<String> list = new ArrayList<String>();

        try{

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select * from SUBDEPARTMENT where DEPARTMENT = '"+department+"'",null);

            if(resultSet.moveToFirst()) {

                do {

                    String dept = resultSet.getString(1);
                    list.add(dept);

                } while (resultSet.moveToNext());

            }

            resultSet.close();
            mydatabase.close();

        }catch(Exception e){

        }

        return list;
    }

    ArrayAdapter<String> getAdap(String dept){

        ArrayAdapter<String> sub_dept_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, fetch_local_subdepartment(dept));
        return sub_dept_adapter;

    }

    public String getSubDeptID(String subdept){

        try{

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select SUBDEPARTMENT_ID from SUBDEPARTMENT where SUBDEPARTMENT = '"+subdept+"'",null);

            Toast.makeText(this, subdept, Toast.LENGTH_SHORT).show();

            String subdept_id = "";
            if(resultSet.moveToFirst())
                subdept_id = resultSet.getString(0);

            resultSet.close();
            mydatabase.close();

            return subdept_id;

        }catch(Exception e){

        }

        return "";
    }

    public boolean addUserToServer(String name, String user_id, String user_password, String subdept, String phone, String address){

        try {

            SharedPreferences sharedpreferences = getSharedPreferences("AppSession", Context.MODE_PRIVATE);

            String user = sharedpreferences.getString("user", "nil");
            String password = sharedpreferences.getString("password", "nil");

            Map<String, String> arg = new HashMap<String, String>();

            String id = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());

            arg.put("admin_id", user);
            arg.put("admin_password", password);
            arg.put("id", user_id);
            arg.put("password", user_password);
            arg.put("name", name);
            arg.put("dp", "");
            arg.put("subdept_id", getSubDeptID(subdept));
            arg.put("phone", phone);
            arg.put("address", address);
            arg.put("DOB", "");

            String res = APIRequest.processRequest(arg, LocalConfig.rootURL + "createUser.php", getApplicationContext());

            Toast.makeText(this, res, Toast.LENGTH_SHORT).show();

            if (res.equals("TRUE")) {

                SQLiteDatabase mydatabase = openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);
                mydatabase.execSQL("insert into USER values('" + user_id + "','" + getSubDeptID(subdept) + "','" + name + "',' ','" + address + "','"+ password +"','"+ phone +"') ");
                mydatabase.close();

                return true;
            } else {
                return false;
            }

        }catch(Exception e){

        }

        return false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Button done;
        final EditText name, reg, password, phone, address;

        first_dept = null;

        done = (Button)findViewById(R.id.done);

        name = (EditText)findViewById(R.id.name);
        reg = (EditText)findViewById(R.id.reg);
        password = (EditText)findViewById(R.id.password);
        phone = (EditText)findViewById(R.id.phone);
        address = (EditText)findViewById(R.id.address);

        dept = (Spinner)findViewById(R.id.dept);
        subdept = (Spinner)findViewById(R.id.subdept);

        ArrayAdapter<String> dept_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, fetch_local_department());

        ArrayAdapter<String> sub_dept_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, fetch_local_subdepartment(first_dept));

        dept.setAdapter(dept_adapter);
        subdept.setAdapter(sub_dept_adapter);

        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                subdept.setAdapter(getAdap(selectedItem));
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uname = name.getText().toString();
                String u_reg = reg.getText().toString();
                String u_password = password.getText().toString();
                String u_phone = phone.getText().toString();
                String u_address = address.getText().toString();

                if(uname != null && u_reg != null && u_password != null && u_phone != null && u_address != null){

                    if(addUserToServer(uname,u_reg,u_password,subdept.getSelectedItem().toString(),u_phone,u_address)){

                        Toast.makeText(AddUser.this, "User created successfully.", Toast.LENGTH_SHORT).show();

                    }else{

                        Toast.makeText(AddUser.this, "User not created.", Toast.LENGTH_SHORT).show();

                    }

                }


            }
        });

    }
}
