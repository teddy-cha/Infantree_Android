package com.connection.next.infantree.db;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.connection.next.infantree.network.ImageDownloadHelper;
import com.connection.next.infantree.provider.InfantreeContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;

/**
 * Created by chayongbin on 15. 4. 15..
 */
public class ProviderDBHelper {

    private static final String TAG = ProviderDBHelper.class.getSimpleName();
    private Context context;

    public ProviderDBHelper(Context context) {
        this.context = context;
    }

    public void insertJsonData(String jsonData) {

        String Photo_Path;
        String Date;

        ImageDownloadHelper imageDownloadHelper = new ImageDownloadHelper(context);

        try {
            JSONArray photoJson = new JSONArray(jsonData);

            /*
             * 실제 서버의 데이터 베이스 꼭 확인해서 변수명 등 변경작업하기.
             * 서버에 파일을 업로드 할 때 유니크 아이디를 받아 파일을 저장할 떄 이름을 바꾸는 작업이 필요함.
             *
             */

            for (int i = 0; i < photoJson.length(); i++) {
                JSONObject jsonObject = photoJson.getJSONObject(i);
                Photo_Path = jsonObject.getString("_id");
                Date = jsonObject.getString("date");

                try {
                    Photo_Path = URLDecoder.decode(Photo_Path, "UTF-8");
                    Date = URLDecoder.decode(Date, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();
                values.put("Photo_Path", Photo_Path);
                values.put("Date", Date);

                Log.e(TAG, InfantreeContract.Photos.CONTENT_URI.toString());
                context.getContentResolver().insert(InfantreeContract.Photos.CONTENT_URI, values);

                imageDownloadHelper.downloadImageFile(Photo_Path);
            }
        } catch (Exception e) {
            Log.e(TAG, "JSON ERROR: " + e);
            e.printStackTrace();
        }
    }

    public void insertDiaryJsonData(String jsonData) {

        String Diary_Id;
        String Photo_Id = null;
        String Mom_Diary = null;
        String Dad_Diary = null;

        try {
            JSONObject diaryJson = new JSONObject(jsonData);

            Diary_Id = diaryJson.getString("_id");

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
