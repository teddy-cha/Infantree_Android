package com.connection.next.infantree.network;

import android.util.Log;

import com.connection.next.infantree.util.ServerUrls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by chayongbin on 15. 4. 23..
 */
public class Proxy {

    private static String TAG = Proxy.class.getSimpleName();

    public static String getJSON(String requestUrl) {
        try {
            Log.e(TAG, "REQUEST URL: " + requestUrl);

            URL url = new URL(requestUrl);
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

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
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

    public static void postDiary(String date, String momDiary, String dadDiary, AsyncHttpResponseHandler responseHandler) {

        String baby_id = "1004";

        RequestParams params = new RequestParams();
        params.put("date", date);
        params.put("baby_id", baby_id);

        if (dadDiary != null)
            params.put("dad_diary", dadDiary);
        if (momDiary != null)
            params.put("mom_diary", momDiary);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(ServerUrls.postDiary, params, responseHandler);
    }
}