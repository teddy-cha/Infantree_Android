package com.connection.next.infantree.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.connection.next.infantree.home.HomeActivity;
import com.connection.next.infantree.login.UsermgmtMainActivity;
import com.kakao.APIErrorResult;
import com.kakao.MeResponseCallback;
import com.kakao.UserManagement;
import com.kakao.UserProfile;
import com.kakao.helper.Logger;

/**
 * 유효한 세션이 있다는 검증 후
 * me를 호출하여 가입 여부에 따라 가입 페이지를 그리던지 Main 페이지로 이동 시킨다.
 * @author chayongbin
 */
public class BaseSignupActivity extends Activity {
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    /**
     * 자동가입앱인 경우는 가입안된 유저가 나오는 것은 에러 상황.
     */
    protected void showSignup() {
        Logger.getInstance().d("not registered user");
        redirectLoginActivity();
    }

    protected void redirectMainActivity() {
//        final Intent intent = new Intent(this, UsermgmtMainActivity.class);
//        startActivity(intent);
//        finish();
        final Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    protected void homeActivity(){

    }

    protected void redirectLoginActivity() {
        Intent intent = new Intent(this, BaseSignupActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {

            @Override
            protected void onSuccess(final UserProfile userProfile) {
                Logger.getInstance().d("UserProfile : " + userProfile);
                userProfile.saveUserToCache();
                if (userProfile.getProperty("baby_name") == null) {
                    redirectMainActivity();
                } else {
                    redirectMainActivity();
                }
            }

            @Override
            protected void onNotSignedUp() {
                Logger.getInstance().d("onNotSignedUP");
                showSignup();
            }

            @Override
            protected void onSessionClosedFailure(final APIErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            protected void onFailure(final APIErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.getInstance().d(message);
                redirectLoginActivity();
            }
        });
    }
}
