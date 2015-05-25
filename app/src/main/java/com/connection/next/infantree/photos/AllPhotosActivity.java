package com.connection.next.infantree.photos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import com.connection.next.infantree.R;

import java.util.ArrayList;

/**
 * Created by chayongbin on 15. 5. 25..
 */
public class AllPhotosActivity extends Activity{

    private AllPhotosAdapter allPhotosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_main);
        GridView gridView = (GridView) findViewById(R.id.grid_view);

        Intent intent = getIntent();
        final String date = intent.getStringExtra("date");
        Log.i("AllPhotosActivity Date is : ", date);

        // Instance of ImageAdapter Class
        gridView.setAdapter(new AllPhotosAdapter(this, date));

        /**
         * On Click event for Single Gridview Item
         * */
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Sending image id to FullScreenActivity
                Intent intent = new Intent("com.connection.next.infantree.photos.FullImageActivity");
                // passing array index
                intent.putExtra("id", position);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

    }
}
