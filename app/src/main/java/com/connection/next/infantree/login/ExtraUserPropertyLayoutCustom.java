/**
 * Copyright 2014 Daum Kakao Corp.
 *
 * Redistribution and modification in source or binary forms are not permitted without specific prior written permission. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.connection.next.infantree.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.connection.next.infantree.R;
import com.gc.materialdesign.views.Button;
import com.kakao.APIErrorResult;
import com.kakao.UpdateProfileResponseCallback;
import com.kakao.UserManagement;
import com.kakao.UserProfile;
import com.kakao.helper.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 추가로 받고자 하는 사용자 정보를 나타내는 layout
 * 이름, 나이, 성별을 입력할 수 있다.
 * @author MJ
 */
public class ExtraUserPropertyLayoutCustom extends Activity {
    // property key
    private  static final String NAME_KEY = "baby_name";
    private  static final String AGE_KEY = "baby_age";
    private  static final String BIRTH_KEY = "baby_birth";
    private  static final String GENDER_KEY = "parent_status";

    private int myYear, myMonth, myDay;

    private EditText name;
    private EditText age;
    private EditText birth;
    private Spinner gender;
    private Button buttonSignup;
    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        userProfile = UserProfile.loadFromCache();
        final View view = getLayoutInflater().inflate(R.layout.dialog, null);

        name = (EditText) view.findViewById(R.id.name);
        age = (EditText) view.findViewById(R.id.age);
        birth = (EditText) view.findViewById(R.id.birth);
//        birthButton = (Button) view.findViewById(R.id.birthButton);
        gender = (Spinner) view.findViewById(R.id.gender);
        buttonSignup = (Button) view.findViewById(R.id.buttonSignup);
        buttonSignup.setOnClickListener(mClickListener);

//        birthButton.setOnClickListener(new View.OnClickListener() {
//
//            final Calendar c = Calendar.getInstance();
//            @Override
//            public void onClick(View v) {
//                myYear = c.get(Calendar.YEAR);
//                myMonth = c.get(Calendar.MONTH);
//                myDay = c.get(Calendar.DAY_OF_MONTH);
//
//                Dialog dlgDate = new DatePickerDialog(context, myDateSetListener,
//                        myYear, myMonth, myDay);
//                dlgDate.show();
//            }
//        });
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final HashMap<String, String> properties = getProperties();

            UserManagement.requestUpdateProfile(new UpdateProfileResponseCallback() {
                @Override
                protected void onSuccess(final long userId) {
                    UserProfile.updateUserProfile(userProfile, properties);
                    if (userProfile != null)
                        userProfile.saveUserToCache();
                    Toast.makeText(getApplicationContext(), "succeeded to update user profile", Toast.LENGTH_SHORT).show();
                    Logger.getInstance().d("succeeded to update user profile" + userProfile);
                }

                @Override
                protected void onSessionClosedFailure(final APIErrorResult errorResult) {
                }

                @Override
                protected void onFailure(final APIErrorResult errorResult) {
                    String message = "failed to update user profile. msg=" + errorResult;
                    Logger.getInstance().d(message);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }, properties);
        }
    };


    HashMap<String, String> getProperties(){
        final String nickNameValue = name.getText().toString();
        final String ageValue = age.getText().toString();
        final String birthValue = birth.getText().toString();
        final String genderValue = String.valueOf(gender.getSelectedItem());

        HashMap<String, String> properties = new HashMap<String, String>();
        if(nickNameValue != null)
            properties.put(NAME_KEY, nickNameValue);
        if(ageValue != null)
            properties.put(AGE_KEY, ageValue);
        if(birthValue != null)
            properties.put(BIRTH_KEY, birthValue);
        if(genderValue != null)
            properties.put(GENDER_KEY, genderValue);

        return properties;
    }

    public void showProperties(final Map<String, String> properties) {
        final String nameValue = properties.get(NAME_KEY);
        if (nameValue != null)
            name.setText(nameValue);

        final String ageValue = properties.get(AGE_KEY);
        if (ageValue != null)
            age.setText(ageValue);

        final String birthValue = properties.get(BIRTH_KEY);
        if (birthValue != null)
            birth.setText(birthValue);

        final String genderValue = properties.get(GENDER_KEY);
        if (genderValue != null) {
            ArrayAdapter<String> myAdap = (ArrayAdapter<String>) gender.getAdapter(); //cast to an ArrayAdapter
            int spinnerPosition = myAdap.getPosition(genderValue);
            gender.setSelection(spinnerPosition);
        }
    }

    public Context getContext() {
        return this;
    }
}
