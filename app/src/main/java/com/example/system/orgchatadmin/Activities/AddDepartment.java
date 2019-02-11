package com.example.system.orgchatadmin.Activities;

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
import com.example.system.orgchatadmin.R;

import java.util.ArrayList;

public class AddDepartment extends AppCompatActivity {

    Button cancel , done;
    EditText dept , sub_dept;
    ImageButton verify , add;
    ListView list;

    ArrayList<String> sub_dept_list;
    DepartmentListAdapter adapter;

    boolean check_sub_department(String sub_department){

        return true;
    }

    boolean verify_department(String department){

        try {



        }catch(SQLException e){
            return false;
        }

        return true;
    }

    boolean add_sub_dept_to_local(){


        return true;
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
                finish();
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

                }

            }
        });

    }
}
