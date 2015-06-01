package com.connection.next.infantree.network;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by chayongbin on 15. 4. 16..
 */
public class ImageDownloadHelper {

    private final Context context;
//    private static AsyncHttpClient client = new AsyncHttpClient();
    public AsyncHttpClient client = new AsyncHttpClient();
    public SyncHttpClient clientSync = new SyncHttpClient();
    public ImageDownloadHelper(Context context) {
        this.context = context;
    }

    public void downloadImageFile(String fileUrl, String file_id) {
        final File filePath = new File(context.getFilesDir().getPath() + "/" + file_id);


//        final File filePath = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + file_id);


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

//            try {
//                URL Url = new URL(fileUrl);
//                URLConnection conn = Url.openConnection();
//                conn.connect();
//                InputStream is = conn.getInputStream();
//
//                BufferedInputStream bis = new BufferedInputStream(is);
//                ByteArrayBuffer baf = new ByteArrayBuffer(50);
//
//                int current = 0;
//
//                while ((current = bis.read()) != -1) {
//                    baf.append((byte) current);
//                }
//
//                FileOutputStream fos = context.openFileOutput(file_id, 0);
//                fos.write(baf.toByteArray());
//                fos.close();
//
//            } catch (IOException e) {
//
//            }
        }
    }

    public void downloadImageFileSync(String fileUrl, String file_id) {
        final File filePath = new File(context.getFilesDir().getPath() + "/" + file_id);


//        final File filePath = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + file_id);


        Log.i("isFileExist", filePath.exists() + " " + filePath.getAbsolutePath());

        if (!filePath.exists()) {
            clientSync.get(fileUrl, new FileAsyncHttpResponseHandler(context) {
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
