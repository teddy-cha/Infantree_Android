package com.connection.next.infantree.home;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;

import com.connection.next.infantree.R;
import com.connection.next.infantree.base.ProfileLayout;
import com.connection.next.infantree.db.PhotoDBHelper;
import com.connection.next.infantree.home.navigation.HomeNavigationAdapter;
import com.connection.next.infantree.login.ExtraUserPropertyLayout;
import com.connection.next.infantree.login.ExtraUserPropertyLayoutCustom;
import com.connection.next.infantree.login.UserMgmtLoginActivity;
import com.connection.next.infantree.model.UserModel;
import com.connection.next.infantree.network.SyncDataService;
import com.gc.materialdesign.views.ButtonFloat;
import com.kakao.APIErrorResult;
import com.kakao.SignupResponseCallback;
import com.kakao.UpdateProfileResponseCallback;
import com.kakao.UserManagement;
import com.kakao.UserProfile;
import com.kakao.helper.Logger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

// import com.urqa.clientinterface.URQAController;

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
    private String parent;

    private UserProfile userProfile;
    private ProfileLayout profileLayout;
    private ExtraUserPropertyLayout extraUserPropertyLayout;

    private Dialog dialog;

    // property key
    private  static final String NAME_KEY = "baby_name";
    private  static final String AGE_KEY = "baby_age";
    private  static final String BIRTH_KEY = "baby_birth";
    private  static final String GENDER_KEY = "parent_status";
    private  static final String BABY_ID = "baby_id";

    private EditText name;
    private EditText age;
    private EditText birth;
    private Spinner gender;
    private Button buttonSignup;
    private DatePicker datePicker;

    private int myYear, myMonth, myDay;
    private String date;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // URQAController.InitializeAndStartSession(getApplicationContext(), "211C55F9");
        setContentView(R.layout.home_main);

        HomeActivity.context = getApplicationContext();

        // ------------------------------------------------
        // register
        // ------------------------------------------------

        userProfile = UserProfile.loadFromCache();

        babyId = userProfile.getProperty("com.kakao.user.properties.baby_id");
        parent = userProfile.getProperty("com.kakao.user.properties.parent_status");
        String a = String.valueOf(userProfile.getProperties());


        // ------------------------------------------------
        // SharedPreferences
        // ------------------------------------------------
        pref = getSharedPreferences(getResources().getString(R.string.pref_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(getResources().getString(R.string.server_url),
                getResources().getString(R.string.server_url_value));
        editor.putString(getResources().getString(R.string.baby_id), babyId);
        editor.putString(getResources().getString(R.string.parent), parent);
        editor.apply();

        serverUrl = pref.getString(context.getResources().getString(R.string.server_url), "");
        babyId = pref.getString(context.getResources().getString(R.string.baby_id), "");

        // ------------------------------------------------
        // Sync Data Setting
        // ------------------------------------------------

        Intent syncIntent = new Intent(context, SyncDataService.class);
        startService(syncIntent);


        // ------------------------------------------------
        // Navigation Bar 에 사용할 Data Model에 대한 값 설정
        // ------------------------------------------------

        UserModel test_model = new UserModel(babyId, userProfile.getProperty("com.kakao.user.properties.baby_name") +  " (" + parent + ")", "생일 " + userProfile.getProperty("com.kakao.user.properties.baby_birth"), userProfile.getProfileImagePath() , R.drawable.bb);

        // ------------------------------------------------
        // Time Line
        // ------------------------------------------------

        recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
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

    private void createDialog() {
        final View innerView = getLayoutInflater().inflate(R.layout.dialog, null);

        name = (EditText) innerView.findViewById(R.id.name);
        age = (EditText) innerView.findViewById(R.id.age);
//        birth = (EditText) innerView.findViewById(R.id.birth);
        gender = (Spinner) innerView.findViewById(R.id.gender);
        buttonSignup = (Button) innerView.findViewById(R.id.buttonSignup);
        datePicker = (DatePicker) innerView.findViewById(R.id.datepicker);
        datePicker.setCalendarViewShown(false);
        datePicker.setSpinnersShown(true);

        dialog = new Dialog(this);
        dialog.setTitle("회원가입");
        dialog.setContentView(innerView);
        dialog.setCancelable(true);


        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
        dialog.show();

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignup(getProperties());
                dialog.cancel();
            }
        });

        datePicker.init(datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myYear = year;
                        myMonth = monthOfYear+1;
                        myDay = dayOfMonth;
                        date = String.format("%d-%02d-%02d", year,monthOfYear+1, dayOfMonth);
                        babyId = String.format("%d%02d%02d", year,monthOfYear+1, dayOfMonth);
                    }
                });
    }

    HashMap<String, String> getProperties(){
        final String nickNameValue = name.getText().toString();
        final String ageValue = age.getText().toString();
        final String birthValue = date.toString();
        final String genderValue = String.valueOf(gender.getSelectedItem());
        final String babyIdValue = babyId.toString();

        HashMap<String, String> properties = new HashMap<String, String>();
        if(nickNameValue != null)
            properties.put(NAME_KEY, nickNameValue);
        if(ageValue != null)
            properties.put(AGE_KEY, ageValue);
        if(birthValue != null)
            properties.put(BIRTH_KEY, birthValue);
        if(genderValue != null)
            properties.put(GENDER_KEY, genderValue);
        if(babyIdValue != null)
            properties.put(BABY_ID, babyIdValue);

        parent = genderValue;
        pref = getSharedPreferences(getResources().getString(R.string.pref_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(getResources().getString(R.string.baby_id), babyId);
        editor.putString(getResources().getString(R.string.parent), parent);
        editor.apply();

        return properties;
    }

    public String sha1(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    protected void redirectLoginActivity() {
        Intent intent = new Intent(this, UserMgmtLoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 가입 입력창의 정보를 모아서 가입 API를 호출한다.
     */
    private void onClickSignup(final HashMap<String, String> properties) {

        UserManagement.requestUpdateProfile(new UpdateProfileResponseCallback() {
            @Override
            protected void onSuccess(final long userId) {
                UserProfile.updateUserProfile(userProfile, properties);
                if (userProfile != null)
                    userProfile.saveUserToCache();
                Toast.makeText(getApplicationContext(), "succeeded to update user profile", Toast.LENGTH_SHORT).show();
                Logger.getInstance().d("succeeded to update user profile" + userProfile);
//                showProfile();
            }

            @Override
            protected void onSessionClosedFailure(final APIErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            protected void onFailure(final APIErrorResult errorResult) {
                String message = "failed to update user profile. msg=" + errorResult;
                Logger.getInstance().d(message);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }, properties);
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
    public void onRestart() {
        super.onRestart();
        userProfile = UserProfile.loadFromCache();
        if(userProfile != null)
            showProfile();
        recyclerView.setAdapter(homeAdapter);
    }

    private void showProfile() {
        if(profileLayout != null)
            profileLayout.setUserProfile(userProfile);
        if(extraUserPropertyLayout != null)
            extraUserPropertyLayout.showProperties(userProfile.getProperties());
    }

    @Override
    public void onResume() {
        super.onResume();
        userProfile = UserProfile.loadFromCache();
        LoadDate();

        recyclerView.setAdapter(homeAdapter);
    }

    private static AsyncHttpClient client = new AsyncHttpClient();


    // ------------------------------------------------
    // serverUrl + "image?baby_id="+ "1004"
    // 1004 부분은 이후 baby_id로 받아야함.
    // ------------------------------------------------
    public void LoadDate() {
        client.get(serverUrl + "image?baby_id=" + babyId, new AsyncHttpResponseHandler() {
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