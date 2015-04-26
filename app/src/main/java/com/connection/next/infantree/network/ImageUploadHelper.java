package com.connection.next.infantree.network;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

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

    public ImageUploadHelper(Context context) {
        this.context = context;
    }

    public void uploadImageFile(final ArrayList<String> imagePaths) {

        RequestParams params = new RequestParams();

        params.put("baby_id", "1004"); // baby_id 들어갈 부분

        for (int i = 0; i < imagePaths.size(); i++) {
            try {
                File file = new File(imagePaths.get(i));
                params.put("upload" + i, file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        client.post("http://125.209.194.223:3000/image", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                for (int i = 0; i < imagePaths.size(); i++) {
                    try {
                        String idFromServer = response.getString("upload" + i);
                        File targetFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), idFromServer);
                        File originalFile = new File(imagePaths.get(i));
                        originalFile.renameTo(targetFile); // 이름 바꿔줌
                        Log.e("upload" + i, idFromServer);
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
