package com.example.system.orgchatadmin.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.system.orgchatadmin.Activities.AddSubDepartment;
import com.example.system.orgchatadmin.Adapters.DepartmentListAdapter;
import com.example.system.orgchatadmin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class SubDepartmentFragment extends Fragment {

    View root;
    ListView dept;
    Button add;
    DepartmentListAdapter adapter;
    ArrayList<String> content;
    String department;

    public SubDepartmentFragment() {
        // Required empty public constructor
    }

    public SubDepartmentFragment(String department){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ArrayList<String> fetch_local_subdepartment(){

        ArrayList<String> list = new ArrayList<String>();

        try{

            SQLiteDatabase mydatabase = getContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select * from SUBDEPARTMENT where DEPARTMENT = department",null);

            if(resultSet.moveToFirst()) {

                do {

                    String dept = resultSet.getString(0);
                    list.add(dept);

                } while (resultSet.moveToNext());

            }

            resultSet.close();
            mydatabase.close();

        }catch(Exception e){

        }

        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_sub_department, container, false);

        dept = (ListView)root.findViewById(R.id.dept_list);
        add = (Button)root.findViewById(R.id.add_dept);
        content = new ArrayList<String>();

        adapter = new DepartmentListAdapter(getContext(),content);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),AddSubDepartment.class));

            }
        });

        dept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return root;
    }

}
