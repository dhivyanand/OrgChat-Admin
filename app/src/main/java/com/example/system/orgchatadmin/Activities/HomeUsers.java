package com.example.system.orgchatadmin.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.system.orgchatadmin.Adapters.UserListAdapter;
import com.example.system.orgchatadmin.R;

import java.util.ArrayList;

public class HomeUsers extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_users);

        getSupportActionBar().setTitle("Users");

        final ArrayList<String> userID,name;
        userID = new ArrayList<String >();
        name = new ArrayList<String>();

        ArrayList<Bitmap> dp = new ArrayList<Bitmap>();

        ListView user = (ListView)findViewById(R.id.list_questions);
        FloatingActionButton add_user = (FloatingActionButton) findViewById(R.id.add_user);

        UserListAdapter adapter = new UserListAdapter(HomeUsers.this,name,dp);

        user.setAdapter(adapter);

        user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                startActivity(new Intent(HomeUsers.this,EditUser.class).putExtra("userID",userID.get(i)));

            }
        });

        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeUsers.this,AddUser.class));
                finish();
            }
        });



        try {

            SQLiteDatabase mydatabase = HomeUsers.this.openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

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
            System.out.println(e.toString());
        }
    }
}
