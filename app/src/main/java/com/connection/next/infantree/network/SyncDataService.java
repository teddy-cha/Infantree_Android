package com.connection.next.infantree.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.connection.next.infantree.db.PhotoDBHelper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chayongbin on 15. 4. 28..
 */
public class SyncDataService extends Service {

    private TimerTask mTask;
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
        mTask = new TimerTask() {
            @Override
            public void run() {
                String jsonData = proxy.getJSON();
                dao.insertJsonData(jsonData);
                Log.i("Sync : ", "Hello");
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 1000*5, 1000*5);
        return super.onStartCommand(intent, flags, startId);
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
