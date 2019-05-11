package com.example.system.orgchatadmin.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.system.orgchatadmin.Activities.AddDepartment;
import com.example.system.orgchatadmin.Activities.HomeNav;
import com.example.system.orgchatadmin.Adapters.DepartmentListAdapter;
import com.example.system.orgchatadmin.R;

import java.io.IOError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class DepartmentFragment extends Fragment {

    View root;
    ListView dept;
    Button add;
    DepartmentListAdapter adapter;
    ArrayList<String> content;

    public DepartmentFragment() {
        // Required empty public constructor
    }

    ArrayList<String> fetch_local_department(){

        ArrayList<String> list = new ArrayList<String>();

        try {

            SQLiteDatabase mydatabase = getContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select * from DEPARTMENT",null);

            if(resultSet.moveToFirst()) {

                do {

                    String dept = resultSet.getString(1);
                    list.add(dept);

                } while (resultSet.moveToNext());

            }

            resultSet.close();
            mydatabase.close();

        }catch(SQLException e){

        }

        return list;

    }

    void departmentList(ListView dept){

        content = fetch_local_department();
        adapter = new DepartmentListAdapter(getContext(),content);
        dept.setAdapter(adapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_department, container, false);

        dept = (ListView)root.findViewById(R.id.dept_list);
        add = (Button)root.findViewById(R.id.add_dept);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),AddDepartment.class));

            }
        });

        dept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String department = content.get(i);

                Fragment subdept = new SubDepartmentFragment(department);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.home_frame, subdept);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        //((HomeNav)getActivity()).setActionBarTitle("Department");

        departmentList(dept);

        return root;
    }

}
