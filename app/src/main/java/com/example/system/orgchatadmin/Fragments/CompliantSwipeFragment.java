package com.example.system.orgchatadmin.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.system.orgchatadmin.Adapters.CircularListAdapter;
import com.example.system.orgchatadmin.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CompliantSwipeFragment extends Fragment {

    public CompliantSwipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_compliant_swipe, container, false);

        ListView list = (ListView)root.findViewById(R.id.compliant_list);

        ArrayList<String> compliant,date;
        compliant = new ArrayList<String>();
        date = new ArrayList<String>();

        CircularListAdapter adapter = new CircularListAdapter(getContext(), compliant, date);

        list.setAdapter(adapter);

        try{

            SQLiteDatabase mydatabase = getContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select TITLE, DATE from MESSAGE where MESSAGE_TYPE = 'C' ",null);

            if(resultSet.moveToFirst()) {

                do {

                    compliant.add(resultSet.getString(0));
                    date.add(resultSet.getString(1));

                    adapter.notifyDataSetChanged();

                } while (resultSet.moveToNext());

            }

            resultSet.close();

        }catch (Exception e){

        }

        return root;

    }

}
