package com.example.system.orgchatadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    boolean verify_admin(String uname , String pass){

        return true;
    }

    private boolean create_local_database(){

        try{

            return true;

        }catch(Exception e){
            return false;
        }

    }

    private boolean create_local_pref(String uname , String pass){

        try {

            SharedPreferences sharedpreferences = getSharedPreferences("AppSession", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("status", "verified");
            editor.putString("user", uname);
            editor.putString("password", pass);
            editor.commit();

            return true;

        }catch(Exception e){
            return false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = (EditText)findViewById(R.id.admin_token);
        final EditText password = (EditText)findViewById(R.id.password);

        Button login = (Button)findViewById(R.id.button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uname = username.getText().toString() , pass = password.getText().toString();

                if (uname != null && pass != null) {

                    if(verify_admin(uname,pass)){

                        if(create_local_pref(uname,pass)){
                            startActivity(new Intent(Login.this,HomeNav.class));
                            finish();
                        }

                    }

                } else
                    Toast.makeText(Login.this, "Please fill in the fields.", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
