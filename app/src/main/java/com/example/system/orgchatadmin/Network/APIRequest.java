package com.example.system.orgchatadmin.Network;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by System on 28/2/19.
 */

public class APIRequest {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public String processRequest(final Map<String,String> arg, final String URL) {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, String.valueOf((JSONObject) arg));
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().toString();
        } catch (Exception e) {
            return null;
        }
    }

}
