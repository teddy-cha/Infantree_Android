package com.connection.next.infantree.home;

import android.content.Intent;
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
import android.widget.Button;

import com.connection.next.infantree.R;
import com.connection.next.infantree.db.ProviderDBHelper;
import com.connection.next.infantree.home.navigation.HomeNavigationAdapter;
import com.connection.next.infantree.model.UserModel;
import com.connection.next.infantree.network.Proxy;
import com.connection.next.infantree.util.ServerUrls;
import com.gc.materialdesign.views.ButtonFloat;

public class HomeActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout drawer;
    ActionBarDrawerToggle mDrawerToggle;

    RecyclerView recyclerView;
    HomeAdapter homeAdapter;

    ButtonFloat floatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        /*
         * Navigation Bar 에 사용할 Data Model에 대한 값을 미리 설정
         */
        UserModel test_model = new UserModel("AX10374561", "차민우", "185일, 6개월", R.drawable.aa, R.drawable.bb);


        /*
         * Time Line
         */
        recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        homeAdapter = new HomeAdapter(R.layout.home_row, this);
        recyclerView.setAdapter(homeAdapter);


        /*
         * Tool Bar
         * Action Bar 설정 및 Navigation Bar
         */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar); // 툴바를 액션바로 설정함
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 액션바 타이틀 안 나오게 함, 타이틀을 툴바에서 처리

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

        ((Button) findViewById(R.id.see_all_photos_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.see_diary_button)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_add_button:
                new AddPhotoDialogFragment().show(getSupportFragmentManager(), "add_photo");
                break;
            case R.id.see_all_photos_button:
                Intent allPhotoIntent = new Intent(this, SeeAllPhotoActivity.class);
                // allPhotoIntent.putExtra("WhichDate", "2015/03/15");
                startActivity(allPhotoIntent);
                break;
            case R.id.see_diary_button:
                // db check 있나 없나 확인하고
                // 있으면 seeDiaryActivity로
                // 없으면 writeDiaryActivity로

                // date 가져오는 부분
                // 일단 여기서 가져와본다

                String date = "2015/05/18 20:16:10"; // photo db엔 이렇게 들어가있음 - 그냥 이대로 넣어주면 된다 바꿔주는 로직은 안에 있음

                Intent diaryIntent = new Intent(this, SeeDiaryActivity.class);
                diaryIntent.putExtra("Date", date);
                startActivity(diaryIntent);
                break;
        }
    }
}