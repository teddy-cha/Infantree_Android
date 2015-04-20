package com.collection.next.infantree.network;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;

/**
 * Created by chayongbin on 15. 4. 16..
 */
public class ImageDownloadHelper {

    private final Context context;
    private static AsyncHttpClient client = new AsyncHttpClient();

    public ImageDownloadHelper(Context context) {
        this.context = context;
    }

    public void downloadImageFile(String fileUrl, String file_id) {
        final File filePath = new File(context.getFilesDir().getPath() + "/" + file_id);

        Log.i("isFileExist", filePath.exists() + " " + filePath.getAbsolutePath());

        if (!filePath.exists()) {
            client.get(fileUrl, new FileAsyncHttpResponseHandler(context) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    Log.i("ImageDownloadHelper", "file:");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    Log.i("ImageDownloadHelper", "success responsePath: " + file.getAbsolutePath());
                    Log.i("ImageDownloadHelper", "success originalPath: " + filePath.getAbsolutePath());
                    file.renameTo(filePath);
                }
            });
        }
    }
}
