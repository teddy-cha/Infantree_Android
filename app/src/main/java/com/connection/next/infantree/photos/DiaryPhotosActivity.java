package com.connection.next.infantree.photos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.connection.next.infantree.R;
import com.connection.next.infantree.network.Proxy;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by chayongbin on 15. 5. 25..
 */
public class DiaryPhotosActivity extends Activity{

    private AllPhotosAdapter allPhotosAdapter;

    private String babyId;
    private String parent;
    private String serverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_main);
        GridView gridView = (GridView) findViewById(R.id.grid_view);

        Intent intent = getIntent();
        final String date = intent.getStringExtra("date");
        final String diaryDate = intent.getStringExtra("diaryDate");
        Log.i("AllPhotosActivity Date is : ", date);

        SharedPreferences pref = getSharedPreferences(getResources().getString(R.string.pref_name), MODE_PRIVATE);
        babyId = pref.getString(getResources().getString(R.string.baby_id), "");
        parent = pref.getString(getResources().getString(R.string.parent), "");
        serverUrl = pref.getString(getResources().getString(R.string.server_url), "");

        String request = serverUrl + "diary?baby_id=" + babyId + "&date=" + diaryDate;

        // Instance of ImageAdapter Class
        gridView.setAdapter(new SelectPhotosAdapter(this, date, request, babyId, diaryDate));


    }
}
