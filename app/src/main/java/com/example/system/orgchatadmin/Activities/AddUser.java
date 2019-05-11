package com.example.system.orgchatadmin.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.system.orgchatadmin.LocalConfig;
import com.example.system.orgchatadmin.Network.APIRequest;
import com.example.system.orgchatadmin.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddUser extends AppCompatActivity {

    String first_dept;
    Spinner dept, subdept;

    Bitmap dpImg;

    ImageView dp;

    public String getSubDeptID(String subdept){

        try{

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select SUBDEPARTMENT_ID from SUBDEPARTMENT where SUBDEPARTMENT = '"+subdept+"'",null);

            String subdept_id = "";
            if(resultSet.moveToFirst())
                subdept_id = resultSet.getString(0);

            resultSet.close();
            mydatabase.close();

            return subdept_id;

        }catch(Exception e){

        }

        return "";
    }

    public boolean addUserToServer(String name, String user_id, String user_password, String subdept, String phone, String address, String dp){

        try {

            SharedPreferences sharedpreferences = getSharedPreferences("AppSession", Context.MODE_PRIVATE);

            String user = sharedpreferences.getString("user", "nil");
            String password = sharedpreferences.getString("password", "nil");

            Map<String, String> arg = new HashMap<String, String>();

            String id = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());

            arg.put("admin_id", user);
            arg.put("admin_password", password);
            arg.put("id", user_id);
            arg.put("password", user_password);
            arg.put("name", name);
            arg.put("dp", dp);
            arg.put("subdept_id", getSubDeptID(subdept));
            arg.put("phone", phone);
            arg.put("address", address);
            arg.put("DOB", "");

            String res = APIRequest.processRequest(arg, LocalConfig.rootURL + "createUser.php", getApplicationContext());

            if (res.equals("TRUE")) {

                SQLiteDatabase mydatabase = openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);
                mydatabase.execSQL("insert into USER values('" + user_id + "','" + getSubDeptID(subdept) + "','" + name + "','" + dp + "','" + address + "','"+ password +"','"+ phone +"') ");
                mydatabase.close();

                return true;
            } else {
                return false;
            }

        }catch(Exception e){

        }

        return false;

    }

    public void pickImage() {
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        FloatingActionButton done;
        final EditText name, reg, password, phone, address, department;

        first_dept = null;
        dpImg = null;

        done = (FloatingActionButton) findViewById(R.id.save);

        name = (EditText)findViewById(R.id.name);
        reg = (EditText)findViewById(R.id.reg);
        password = (EditText)findViewById(R.id.password);
        phone = (EditText)findViewById(R.id.phone);
        address = (EditText)findViewById(R.id.address);
        department = (EditText)findViewById(R.id.department);

        dp = (ImageView)findViewById(R.id.dp);

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uname = name.getText().toString();
                String u_reg = reg.getText().toString();
                String u_password = password.getText().toString();
                String u_phone = phone.getText().toString();
                String u_address = address.getText().toString();

                if(uname != null && u_reg != null && u_password != null && u_phone != null && u_address != null){

                    String image = "";

                    if(dpImg != null)
                        image = BitMapToString(dpImg);

                    //Toast.makeText(AddUser.this, image, Toast.LENGTH_SHORT).show();

                    if(addUserToServer(uname,u_reg,u_password,department.getText().toString(),u_phone,u_address,image)){

                        Toast.makeText(AddUser.this, "User created successfully.", Toast.LENGTH_SHORT).show();
                        finish();

                    }else{

                        Toast.makeText(AddUser.this, "User not created.", Toast.LENGTH_SHORT).show();

                    }

                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                dpImg = bitmap;

                dp.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
