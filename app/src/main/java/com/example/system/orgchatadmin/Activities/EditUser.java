package com.example.system.orgchatadmin.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditUser extends AppCompatActivity {

    String first_dept, profile;
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

            String res = APIRequest.processRequest(arg, LocalConfig.rootURL + "editUser.php", getApplicationContext());

            if (res.equals("TRUE")) {

                SQLiteDatabase mydatabase = openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);
                mydatabase.execSQL("delete from USER where USER_ID = '"+user_id+"' ");
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
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);


        FloatingActionButton done;
        final EditText name, reg, password, phone, address, department;
        String spinner_Dept, spinner_subDept;

        first_dept = null;

        done = (FloatingActionButton) findViewById(R.id.save);

        name = (EditText)findViewById(R.id.name);
        reg = (EditText)findViewById(R.id.reg);
        password = (EditText)findViewById(R.id.password);
        phone = (EditText)findViewById(R.id.phone);
        address = (EditText)findViewById(R.id.address);
        department = (EditText)findViewById(R.id.department);

        dp = (ImageView)findViewById(R.id.dp);

        String userID = getIntent().getStringExtra("userID");

        reg.setText(userID);

        try{

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select * from USER where USER_ID = '"+userID+"' ",null);

            if(resultSet.moveToFirst()) {

                department.setText(resultSet.getString(1));

                name.setText(resultSet.getString(2));

                profile = resultSet.getString(3);

                if(profile != null)
                    dp.setImageBitmap(StringToBitMap(profile));

                phone.setText(resultSet.getString(6));
                address.setText(resultSet.getString(4));
                password.setText(resultSet.getString(5));

            }

            resultSet.close();

        }catch (Exception e){

        }

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

                    String image = profile;

                    if(dpImg != null)
                        image = BitMapToString(dpImg);
                    //else
                    //    image = BitMapToString(BitmapFactory.decodeResource(getResources(),R.drawable.ic_profile));

                    if(addUserToServer(uname,u_reg,u_password,department.getText().toString(),u_phone,u_address,image)){

                        Toast.makeText(EditUser.this, "User created successfully.", Toast.LENGTH_SHORT).show();
                        finish();

                    }else{

                        Toast.makeText(EditUser.this, "User not created.", Toast.LENGTH_SHORT).show();

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
