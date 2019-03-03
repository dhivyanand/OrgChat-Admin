package com.example.system.orgchatadmin.Network;

import android.content.Context;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

        return res.get(0);

    }

}
