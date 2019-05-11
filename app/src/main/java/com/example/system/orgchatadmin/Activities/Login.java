package com.example.system.orgchatadmin.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.system.orgchatadmin.Database.CreateDatabaseUsingHelper;
import com.example.system.orgchatadmin.Functions.HttpRequestFunction;
import com.example.system.orgchatadmin.LocalConfig;
import com.example.system.orgchatadmin.Network.APIRequest;
import com.example.system.orgchatadmin.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    boolean verify_admin(String uname , String pass) {

        Map<String,String> req = new HashMap<String,String>();
        req.put("id",uname);
        req.put("password",pass);

        String res = APIRequest.processRequest(req,LocalConfig.rootURL+"adminLogin.php",getApplicationContext());

        JSONObject obj = null;

        try {

            obj = new JSONObject(res);

            String result = (String)obj.get("result");

            if(result.equals("TRUE"))
                return true;
            else
                return false;

        } catch (Exception e) {
            return false;
        }

    }

    private boolean create_local_database(){

        try{

            CreateDatabaseUsingHelper db = new CreateDatabaseUsingHelper(getApplicationContext());
            db.getWritableDatabase();
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

                if (uname != "" && pass != "") {

                    if(verify_admin(uname,pass)){

                        if(create_local_pref(uname,pass) && create_local_database()){
                            startActivity(new Intent(Login.this,home.class));
                            Toast.makeText(Login.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }

                } else
                    Toast.makeText(Login.this, "Please fill in the fields.", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
