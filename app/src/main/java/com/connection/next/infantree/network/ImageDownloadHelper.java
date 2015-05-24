package com.connection.next.infantree.network;

import android.content.Context;
import android.util.Log;

import com.connection.next.infantree.util.ServerUrls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;

import java.io.File;

/**
 * Created by chayongbin on 15. 4. 16..
 */
public class ImageDownloadHelper {

    private final Context context;
    private static SyncHttpClient client = new SyncHttpClient();

    public ImageDownloadHelper(Context context) {
        this.context = context;
    }

    public void downloadImageFile(String photoId) {
        final File filePath = new File(context.getFilesDir().getPath() + "/" + photoId);

        Log.i("isFileExist", filePath.exists() + " " + filePath.getAbsolutePath());

        if (!filePath.exists()) {
            client.get(ServerUrls.getPhotoFile + photoId, new FileAsyncHttpResponseHandler(context) {
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
