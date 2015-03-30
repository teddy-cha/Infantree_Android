package com.example.viz.infantree;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends FragmentActivity {

    private final String ARG_SELECTED_LAYOUT_ID = "selectedLayoutId";

    private final int DEFAULT_LAYOUT = R.layout.layout_spannable_grid;

    private int mSelectedLayoutId;

    private LayoutFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSelectedLayoutId = DEFAULT_LAYOUT;
        if (savedInstanceState != null) {
            mSelectedLayoutId = savedInstanceState.getInt(ARG_SELECTED_LAYOUT_ID);
        }

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

//        LayoutFragment mFragment = new LayoutFragment();
        mFragment = (LayoutFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        if (mFragment == null) {
            mFragment = (LayoutFragment) LayoutFragment.newInstance(R.layout.layout_spannable_grid);
            ft.add(R.id.content, mFragment);
            ft.commit();
        } else {
            ft.attach(mFragment);
        }

//
//        if (mFragment == null) {
//            mFragment = (LayoutFragment) LayoutFragment.newInstance(R.layout.layout_spannable_grid);
//            fm.beginTransaction()
//                    .add(R.id.list, new LayoutFragment())
//                    .commit();
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_SELECTED_LAYOUT_ID, mSelectedLayoutId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
