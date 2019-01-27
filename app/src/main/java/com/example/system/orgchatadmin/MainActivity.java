package com.example.system.orgchatadmin;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db=openOrCreateDatabase("OrgChat", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists profile(name varchar(45),id varchar(45),dp varchar(45),password varchar(45))");
        db.execSQL("create table if not exists message(sender varchar(45) , message blob , message_type varchar(45) , message_description varchar(45), department varchar(45) , sub_department varchar(45) , date varchar(45) , time varchar(45) , status varchar(45))");
        db.execSQL("create table if not exists user(name varchar(45), id varchar(45) , password varchar(45) , dp blob , dept varchar(45) , subdept varchar(45))");
        Cursor cur = db.rawQuery("SELECT * FROM profile", null);
        if(cur.moveToFirst()){
            Constants.admin_id= cur.getString(cur.getColumnIndex("id"));
            Constants.admin_password = cur.getString(cur.getColumnIndex("password"));
            startActivity(new Intent(MainActivity.this,home.class));
        }else{
            startActivity(new Intent(MainActivity.this,Login.class));
        }

        cur.close();
        db.close();
        finish();


    }
}
