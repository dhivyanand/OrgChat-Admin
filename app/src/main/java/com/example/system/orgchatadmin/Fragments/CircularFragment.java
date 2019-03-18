package com.example.system.orgchatadmin.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.system.orgchatadmin.Activities.NewCircular;
import com.example.system.orgchatadmin.Adapters.CircularListAdapter;
import com.example.system.orgchatadmin.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class CircularFragment extends Fragment {

    public CircularFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_circular, container, false);

        Button new_circular = (Button)root.findViewById(R.id.new_circular);
        ListView circular_list = (ListView)root.findViewById(R.id.circular_list);

        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> date = new ArrayList<String>();

        CircularListAdapter adap = new CircularListAdapter(getContext(),list,date);

        circular_list.setAdapter(adap);

        try {

            SQLiteDatabase mydatabase = getContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select TITLE, DATE from CIRCULAR",null);

            if(resultSet.moveToFirst()) {

                do {

                    list.add(resultSet.getString(0));
                    date.add(resultSet.getString(1));

                    adap.notifyDataSetChanged();

                } while (resultSet.moveToNext());

            }

            resultSet.close();
            //mydatabase.close();

        }catch(Exception e){
            System.out.println(e.toString());
        }



        new_circular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),NewCircular.class));
            }
        });

        return root;

    }

}
