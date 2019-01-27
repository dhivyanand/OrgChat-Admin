package com.example.system.orgchatadmin;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

                if(username.getText() != null && password.getText() != null){

                    RequestQueue queue = Volley.newRequestQueue(Login.this);
                    final String url = LocalConfig.rootURL+"adminLogin.php";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        if (response.get("result").toString().equals("Login Successful")) {
                                            SQLiteDatabase db = openOrCreateDatabase("OrgChat", Context.MODE_PRIVATE, null);
                                            db.execSQL("insert into profile values('"+response.get("name").toString()+"','"+username.getText().toString()+"','"+response.get("dp").toString()+"','"+password.getText().toString()+"')");

                                            db.close();
                                            startActivity(new Intent(Login.this,home.class));
                                            finish();
                                        }else{
                                            Toast.makeText(Login.this, "Unable to Login.", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        //Toast.makeText(Login.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();

                                }
                            }){
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("id", username.getText().toString());
                            params.put("password", password.getText().toString());

                            return params;
                        }
                    };

                    queue.add(jsonObjectRequest);


                }else{
                    Toast.makeText(Login.this, "Fill in the fields.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
