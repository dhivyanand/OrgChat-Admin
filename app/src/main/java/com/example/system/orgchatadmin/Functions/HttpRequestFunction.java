package com.example.system.orgchatadmin.Functions;



import com.example.system.orgchatadmin.LocalConfig;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequestFunction {

    public static void createDepartment(String id, String password, String department, String url){

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("id",id)
                .add("password",password)
                .add("new_dept",department)
                .build();
        Request request = new Request.Builder()
                .url(LocalConfig.rootURL+"createDepartment.php")
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String res = response.toString();

            if(res.toString() == "TRUE"){
                    sync_department(id,password);
            }
            // Do something with the response.
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void sync_department(String id, String password){

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("id",id)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url(LocalConfig.rootURL+"syncDept.php")
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String res = response.toString();

            // Do something with the response.
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
