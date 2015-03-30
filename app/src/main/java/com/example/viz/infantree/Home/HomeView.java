package com.example.viz.infantree.Home;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import com.example.viz.infantree.R;


public class HomeView extends FragmentActivity {

    private LayoutFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        mFragment = (LayoutFragment) getSupportFragmentManager().findFragmentById(R.id.content);

        if (mFragment == null) {
            mFragment = (LayoutFragment) LayoutFragment.newInstance(R.layout.home_spannable_gird);
            ft.add(R.id.content, mFragment);
            ft.commit();
        } else {
            ft.attach(mFragment);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
