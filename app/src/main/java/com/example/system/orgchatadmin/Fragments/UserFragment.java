package com.example.system.orgchatadmin.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.system.orgchatadmin.Activities.AddUser;
import com.example.system.orgchatadmin.Adapters.UserListAdapter;
import com.example.system.orgchatadmin.Activities.EditUser;
import com.example.system.orgchatadmin.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class UserFragment extends Fragment {



    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user, container, false);

        final ArrayList<String> userID,name;
        userID = new ArrayList<String >();
        name = new ArrayList<String>();

        ArrayList<Bitmap> dp = new ArrayList<Bitmap>();

        ListView user = (ListView)root.findViewById(R.id.list_questions);
        Button add_user = (Button)root.findViewById(R.id.add_user);

        UserListAdapter adapter = new UserListAdapter(getContext(),name,dp);

        user.setAdapter(adapter);

        user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getContext(), userID.get(i)+" "+i, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(),EditUser.class).putExtra("userID",userID.get(i)));

            }
        });

        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AddUser.class));
            }
        });



        try {

            SQLiteDatabase mydatabase = getContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select USER_ID, NAME from USER",null);

            if(resultSet.moveToFirst()) {

                do {

                    userID.add(resultSet.getString(0));
                    name.add(resultSet.getString(1));

                    //Bitmap b = StringToBitMap(resultSet.getString(2));

                   // if(b != null)
                   //     dp.add(b);
                   // else
                    //    dp.add(BitmapFactory.decodeResource(getResources(),R.drawable.ic_profile));

                    adapter.notifyDataSetChanged();

                } while (resultSet.moveToNext());

            }

            resultSet.close();
            //mydatabase.close();

        }catch(Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            System.out.println(e.toString());
        }

        return root;

    }

}
