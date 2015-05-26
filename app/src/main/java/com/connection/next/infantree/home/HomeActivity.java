package com.connection.next.infantree.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.connection.next.infantree.db.PhotoDBHelper;
import com.connection.next.infantree.home.navigation.HomeNavigationAdapter;
import com.connection.next.infantree.R;
import com.connection.next.infantree.model.UserModel;
import com.connection.next.infantree.network.SyncDataService;
import com.gc.materialdesign.views.ButtonFloat;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.gc.materialdesign.views.ButtonFloat;
import com.urqa.clientinterface.URQAController;

import org.apache.http.Header;

// ActionBarActivity -> appcompat 사용
public class HomeActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private static Context context;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout drawer;
    ActionBarDrawerToggle mDrawerToggle;
    RecyclerView recyclerView;
    HomeAdapter homeAdapter;

    ButtonFloat floatingButton;

    private SharedPreferences pref;
    private String serverUrl;
    private String babyId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        URQAController.InitializeAndStartSession(getApplicationContext(),"211C55F9");
        setContentView(R.layout.home_main);
        HomeActivity.context = getApplicationContext();

        // ------------------------------------------------
        // SharedPreferences
        // ------------------------------------------------

        pref = getSharedPreferences(getResources().getString(R.string.pref_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(getResources().getString(R.string.server_url),
                getResources().getString(R.string.server_url_value));
        editor.putString(getResources().getString(R.string.baby_id), "1004");
        editor.commit();

        serverUrl = pref.getString(context.getResources().getString(R.string.server_url), "");
        babyId = pref.getString(context.getResources().getString(R.string.baby_id), "");

        // ------------------------------------------------
        // Sync Data Setting
        // ------------------------------------------------

//        Intent syncIntent = new Intent(context, SyncDataService.class);
//        startService(syncIntent);


        // ------------------------------------------------
        // Navigation Bar 에 사용할 Data Model에 대한 값 설정
        // ------------------------------------------------

        UserModel test_model = new UserModel(babyId, "차민우", "185일, 6개월", R.drawable.aa, R.drawable.bb);

        // ------------------------------------------------
        // Time Line
        // ------------------------------------------------

        recyclerView = (RecyclerView)findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        homeAdapter = new HomeAdapter(R.layout.home_row, this);
        recyclerView.setAdapter(homeAdapter);

        // ------------------------------------------------
        // Tool Bar
        // Action Bar 설정 및 Navigation Bar
        // ------------------------------------------------

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new HomeNavigationAdapter(test_model);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        floatingButton = (ButtonFloat) findViewById(R.id.floating_add_button);
        floatingButton.setOnClickListener(this);


    }

    // ------------------------------------------------
    // plus button action
    // ------------------------------------------------

    @Override
    public void onClick(View v) {
        new AddPhotoDialogFragment().show(getSupportFragmentManager(), "add_photo");
    }


    // ------------------------------------------------
    // 임시적으로 사용하는 함수
    // ------------------------------------------------

    @Override
    public void onRestart(){
        super.onRestart();
        recyclerView.setAdapter(homeAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadDate();
        recyclerView.setAdapter(homeAdapter);
    }
    private static AsyncHttpClient client = new AsyncHttpClient();


    // ------------------------------------------------
    // serverUrl + "image?baby_id="+ "1004"
    // 1004 부분은 이후 baby_id로 받아야함.
    // ------------------------------------------------
    public void LoadDate() {
        client.get(serverUrl + "image?baby_id="+ babyId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                PhotoDBHelper dao = new PhotoDBHelper(getApplicationContext());
                String jsonData = new String(responseBody);
                Log.i("test", "jsonData: " + jsonData);
                dao.insertJsonData(jsonData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public static Context getAppContext() {
        return HomeActivity.context;
    }
}