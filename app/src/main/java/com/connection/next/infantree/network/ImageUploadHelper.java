package com.connection.next.infantree.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.connection.next.infantree.R;
import com.connection.next.infantree.db.PhotoDBHelper;
import com.connection.next.infantree.home.HomeActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by chayongbin on 15. 4. 16..
 */
public class ImageUploadHelper {

    private final Context context;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private PhotoDBHelper dao;
    private Proxy proxy;

    private SharedPreferences pref;
    private String serverUrl;
    private String babyId;

    public ImageUploadHelper(Context context) {
        this.context = context;
        String prefName = context.getResources().getString(R.string.pref_name);
        pref = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
        serverUrl = pref.getString(context.getResources().getString(R.string.server_url), "");
        babyId = pref.getString(context.getResources().getString(R.string.baby_id), "");
    }

    public void uploadImageFile(final ArrayList<String> imagePaths) {

        dao = new PhotoDBHelper(HomeActivity.getAppContext());

        RequestParams params = new RequestParams();

        params.put("baby_id", babyId); // baby_id 들어갈 부분

        for (int i = 0; i < imagePaths.size(); i++) {
            try {
                File file = new File(imagePaths.get(i));
                params.put("upload" + i, file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        client.post(serverUrl + "image", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                for (int i = 0; i < imagePaths.size(); i++) {
                    try {
                        String idFromServer = response.getString("upload" + i);
                        File targetFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), idFromServer);
                        File originalFile = new File(imagePaths.get(i));
                        originalFile.renameTo(targetFile); // 이름 바꿔줌
                        Log.i("upload" + i, idFromServer);
                        // TODO: imageDB에 넣어주기 or 서버에서 갱신해오기

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("image upload", "failed!");
            }
        });
    }
}