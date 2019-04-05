package com.example.system.orgchatadmin.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.system.orgchatadmin.Activities.MessageActivity;
import com.example.system.orgchatadmin.Adapters.CircularListAdapter;
import com.example.system.orgchatadmin.Adapters.MessageAdapter;
import com.example.system.orgchatadmin.LocalConfig;
import com.example.system.orgchatadmin.R;
import com.example.system.orgchatadmin.Services.ApplicationBackgroundService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.layout.simple_list_item_1;
import static android.content.Context.MODE_PRIVATE;


public class CompliantFragment extends Fragment {

    public CompliantFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public String getUserID(){

        SharedPreferences sharedpreferences = getContext().getSharedPreferences("AppSession", Context.MODE_PRIVATE);

        return sharedpreferences.getString("user","nil");

    }

    public String getUserPassword(){

        SharedPreferences sharedpreferences = getContext().getSharedPreferences("AppSession", Context.MODE_PRIVATE);

        return sharedpreferences.getString("password","nil");

    }

    public String getDeptFromId(String id){

        String name = "";

        try {

            SQLiteDatabase mydatabase = getContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select DEPARTMENT , SUBDEPARTMENT from SUBDEPARTMENT where SUBDEPARTMENT_ID = '"+id+"' ",null);

            if(resultSet.moveToFirst()) {
                name += resultSet.getString(0);
                name += " > ";
                name += resultSet.getString(1);
            }
            resultSet.close();
            mydatabase.close();

        }catch(Exception e){
            System.out.println(e.toString());
        }

        return name;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_compliant, container, false);

        new ApplicationBackgroundService().sync_messages(getUserID(),getUserPassword());

        ListView list = (ListView)root.findViewById(R.id.compliant_list);

        final ArrayList<String> compliant,date,message_id,sender_id,subdept_id;
        compliant = new ArrayList<String>();
        date = new ArrayList<String>();
        subdept_id = new ArrayList<String>();
        message_id = new ArrayList<String>();
        sender_id = new ArrayList<String>();

        MessageAdapter adapter = new MessageAdapter(getContext(), compliant, date, subdept_id);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getContext(), MessageActivity.class);

                intent.putExtra("message_id",message_id.get(i));
                intent.putExtra("user_id",sender_id.get(i));
                intent.putExtra("message_type","Compliant");

                startActivity(intent);

            }
        });

        try{

            SQLiteDatabase mydatabase = getContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select TITLE, TIME, MESSAGE_ID, SENDER_ID, SUBDEPARTMENT_ID from MESSAGE where MESSAGE_TYPE = 'C' ",null);

            if(resultSet.moveToFirst()) {

                do {

                    compliant.add(resultSet.getString(0));
                    date.add(resultSet.getString(1));
                    message_id.add(resultSet.getString(2));
                    sender_id.add(resultSet.getString(3));
                    subdept_id.add(getDeptFromId(resultSet.getString(4)));

                    adapter.notifyDataSetChanged();

                } while (resultSet.moveToNext());

            }

            resultSet.close();

        }catch (Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        return root;
    }

}
