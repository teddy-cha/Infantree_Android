package com.connection.next.infantree.db;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.connection.next.infantree.provider.InfantreeContract;

import org.json.JSONObject;

/**
 * Created by chayongbin on 15. 4. 15..
 */
public class ProviderDBHelper {

    private static final String TAG = ProviderDBHelper.class.getSimpleName();
    private Context context;

    public ProviderDBHelper(Context context) {
        this.context = context;
    }

    public void insertDiaryJsonData(String jsonData) {

        String Diary_Id;
        String Photo_Id = null;
        String Mom_Diary = null;
        String Dad_Diary = null;

        try {
            JSONObject diaryJson = new JSONObject(jsonData);
            if (diaryJson.isNull("date")) {
                Log.e(TAG, "date from json is null!");
                return;
            }

            Diary_Id = diaryJson.getString("date");

            // null이 아닐 때만 값 추출
            if (!diaryJson.isNull("today_photo"))
                Photo_Id = diaryJson.getString("today_photo");
            if (!diaryJson.isNull("mom_diary"))
                Mom_Diary = diaryJson.getString("mom_diary");
            if (!diaryJson.isNull("dad_diary"))
                Dad_Diary = diaryJson.getString("dad_diary");

            ContentValues values = new ContentValues();
            values.put(InfantreeContract.Diaries.DIARY_ID, Diary_Id);
            values.put(InfantreeContract.Diaries.PHOTO_ID, Photo_Id);
            values.put(InfantreeContract.Diaries.MOM_DIARY, Mom_Diary);
            values.put(InfantreeContract.Diaries.DAD_DIARY, Dad_Diary);

            Log.e(TAG, InfantreeContract.Diaries.CONTENT_URI.toString());
            context.getContentResolver().insert(InfantreeContract.Diaries.CONTENT_URI, values);

        } catch (Exception e) {
            Log.e(TAG, "JSON ERROR: " + e);
            e.printStackTrace();
        }
    }
}
