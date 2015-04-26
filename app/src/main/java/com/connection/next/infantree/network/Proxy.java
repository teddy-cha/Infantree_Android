package com.connection.next.infantree.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by chayongbin on 15. 4. 23..
 */
public class Proxy {

    private String serverUrl;
    private Context context;

    public Proxy(Context context) {
        this.context = context;
    }

    public String getJSON() {
        try {
            serverUrl = "http://125.209.194.223:3000/image?baby_id=1004";
            URL url = new URL(serverUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);

            conn.connect();

            int status = conn.getResponseCode();
            Log.i("test", "ProxyResponseCode: " + status);

            switch(status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
