package com.connection.next.infantree.network;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.connection.next.infantree.db.PhotoDBHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chayongbin on 15. 4. 28..
 */
public class SyncDataService extends Service {

    private TimerTask mTask;
    private AsyncTask task;
    private Timer mTimer;
    private Proxy proxy;
    private PhotoDBHelper dao;

    @Override
    public void onCreate() {
        super.onCreate();

        proxy = new Proxy(getApplicationContext());
        dao = new PhotoDBHelper(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Thread a = new Thread(){
            @Override
            public void run(){
                while(true){
                    String jsonData = proxy.getJSON();
                    dao.insertJsonData(jsonData);
                    Log.i("Sync : ", "Hello");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        a.start();
        return super.onStartCommand(intent, flags, startId);
    }


//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        task = new AsyncTask() {
//            @Override
//            protected Object doInBackground(Object[] params) {
//                return null;
//            }
//        };
//
//        mTask = new TimerTask() {
//            @Override
//            public void run() {
//
//                LoadDate();
//                Log.i("Sync : ", "Hello");
//            }
//        };
//
//        mTimer = new Timer();
//        mTimer.schedule(mTask, 1000*5, 1000*5);
//        return super.onStartCommand(intent, flags, startId);
//    }

    private static AsyncHttpClient client = new AsyncHttpClient();

    private void LoadDate() {
        client.get("http://125.209.194.223:3000/image?baby_id=1004", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonData = new String(responseBody);
                dao.insertJsonData(jsonData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
