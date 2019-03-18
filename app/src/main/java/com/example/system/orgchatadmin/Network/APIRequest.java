package com.example.system.orgchatadmin.Network;

import android.content.Context;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;

/**
 * Created by System on 28/2/19.
 */

public class APIRequest {

    public static String mapToString(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            String value = map.get(key);
            try {
                stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
                stringBuilder.append("=");
                stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }

        return stringBuilder.toString();
    }

    public static String processRequest(final Map<String, String> arg, final String URL, final Context context) {

        final ArrayList<String> res = new ArrayList<String>();

        String response="null";

        Thread t = new Thread(){

            @Override
            public void run(){

                try{

                    URL obj = new URL(URL);
                    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                    //add reuqest header
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                    String urlParameters = mapToString(arg);

                    // Send post request
                    con.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();

                    DataInputStream read = new DataInputStream(con.getInputStream());

                    String line;
                    StringBuffer stringBuffer = new StringBuffer();

                    while( (line = read.readLine()) != null ){
                        stringBuffer.append(line);
                    }

                    res.add(stringBuffer.toString());

                }catch(Exception e){

                }

            }

        };

        try {
            t.start();
            t.join();
        }catch (Exception e){

        }

        if(res.size() > 0)
            return res.get(0);
        else
            return "null";
        

    }

    public static String processRequest(final Map<String,String> arg, final Map<String,File> attachment, final String url, final Context context) {


        final ArrayList<String> res = new ArrayList<String>();

        Thread t = new Thread() {

            @Override
            public void run() {

                try {


                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);

                    MultipartEntity reqEntity = new MultipartEntity();

                    for (Map.Entry entry : arg.entrySet())
                        reqEntity.addPart(entry.getKey().toString(), new StringBody(entry.getValue().toString()));

                    for (Map.Entry entry : attachment.entrySet()) {
                        reqEntity.addPart(entry.getKey().toString(), new FileBody(attachment.get(entry.getKey())));
                        //Toast.makeText(context, entry.getKey().toString(), Toast.LENGTH_SHORT).show();
                    }

                    post.setEntity(reqEntity);

                    HttpResponse response = client.execute(post);
                    HttpEntity resEntity = response.getEntity();
                    final String response_str = EntityUtils.toString(resEntity);

                    res.add(response_str);

                } catch (final IOException e) {
                    System.out.println(e.toString());
                }

            }

        };
        try {
            t.start();
            t.join();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return res.get(0);

    }

}
