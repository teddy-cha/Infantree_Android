package com.connection.next.infantree.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.connection.next.infantree.network.ImageDownloadHelper;
import com.connection.next.infantree.provider.PhotoContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;

/**
 * Created by chayongbin on 15. 4. 15..
 */
public class ProviderDBHelper {

    private static final String TAG = ProviderDBHelper.class.getSimpleName();
    private Context context;
    private SharedPreferences sharedPreferences;

    public void insertJsonData(String jsonData) {
        String Photo_Id;
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
                Photo_Id = jsonObject.getString("Photo_Id");
                Photo_Path = jsonObject.getString("Photo_Path");
                Date = jsonObject.getString("Date");

                try {
                    Photo_Id = URLDecoder.decode(Photo_Id, "UTF-8");
                    Photo_Path = URLDecoder.decode(Photo_Path, "UTF-8");
                    Date = URLDecoder.decode(Date, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();
                values.put("Photo_Id", Photo_Id);
                values.put("Photo_Path", Photo_Path);
                values.put("Date", Date);

                context.getContentResolver().insert(PhotoContract.Photo.CONTENT_URI, values);

                imageDownloadHelper.downloadImageFile("http://125.209.194.223:3000/image?_id=" + Photo_Id , Photo_Id);
            }
        } catch (Exception e) {
            Log.e(TAG, "JSON ERROR: " + e);
            e.printStackTrace();
        }
    }
}
