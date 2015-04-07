package com.example.viz.infantree.home;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.viz.infantree.home.navigation.HomeNavigationAdapter;
import com.example.viz.infantree.R;

// ActionBarActivity -> appcompat 사용
public class HomeActivity extends ActionBarActivity {

    // nav home_row_item 들어갈 내용
    String TITLES[] = {"타임라인", "필터", "설정"};
    int ICONS[] = {R.drawable.ic_access_time_grey600_24dp,
            R.drawable.ic_filter_list_grey600_24dp,
            R.drawable.ic_settings_grey600_24dp};

    String NAME = "차민우";
    String AGE = "185일, 6개월";
    int PROFILE = R.drawable.aka;

    private Toolbar toolbar;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout drawer;
    ActionBarDrawerToggle mDrawerToggle;

    RecyclerView recyclerView;
    HomeAdapter homeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        recyclerView = (RecyclerView)findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        homeAdapter = new HomeAdapter(R.layout.home_row, this);
        recyclerView.setAdapter(homeAdapter);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar); // 툴바를 액션바로 설정함
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 액션바 타이틀 안 나오게 함, 타이틀을 툴바에서 처리

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new HomeNavigationAdapter(TITLES, ICONS, NAME, AGE, PROFILE);
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


//        recyclerViewe = (RecyclerView)findViewById(R.id.home_recycler_view);
//        recyclerViewe.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewe.setItemAnimator(new DefaultItemAnimator());
//
//        homeAdapter = new HomeAdapter(R.layout.home_row, this);
//        mRecyclerView.setAdapter(homeAdapter);
    }
}