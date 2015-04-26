package com.connection.next.infantree.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.connection.next.infantree.model.PhotoModel;
import com.connection.next.infantree.network.ImageDownloadHelper;
import com.connection.next.infantree.provider.PhotoContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;

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
        String date;

        ImageDownloadHelper imageDownloadHelper = new ImageDownloadHelper(context);

        try {
            JSONArray photoJson = new JSONArray(jsonData);

            /*
             * 실제 서버의 데이터 베이스 꼭 확인해서 변수명 등 변경작업하기.
             * 서버에 파일을 업로드 할 때 유니크 아이디를 받아 파일을 저장할 떄 이름을 바꾸는 작업이 필요함.
             */

            for (int i = 0; i < photoJson.length(); i++) {
                JSONObject jsonObject = photoJson.getJSONObject(i);
                Photo_Path = jsonObject.getString("_id");
                date = jsonObject.getString("date");
                System.out.println("----------Insert Json Data-----------");
                System.out.println("Photo_Path is " + Photo_Path);
                System.out.println("date id " + date);


                try {
                    Photo_Path = URLDecoder.decode(Photo_Path, "UTF-8");
                    date = URLDecoder.decode(date, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();
                values.put("Photo_Path", Photo_Path);
                values.put("date", date);

                context.getContentResolver().insert(PhotoContract.Photo.CONTENT_URI, values);

                imageDownloadHelper.downloadImageFile("http://125.209.194.223:3000/image?_id=" + Photo_Path , Photo_Path);
            }
        } catch (Exception e) {
            Log.e(TAG, "JSON ERROR: " + e);
            e.printStackTrace();
        }
    }

    public ArrayList<PhotoModel> getArrayList() {
        ArrayList<PhotoModel> photoList = new ArrayList<PhotoModel>();

        String Photo_Id;
        String Photo_Path;
        String date;

        Cursor cursor = context.getContentResolver().query(PhotoContract.Photo.CONTENT_URI,
                PhotoContract.Photo.PROJECTION_ALL,null, null,
                PhotoContract.Photo._ID + " ASC");

        if (cursor != null) {
            cursor.moveToFirst();
            while (!(cursor.isAfterLast())) {
                Photo_Id = cursor.getString(0);
                Photo_Path = cursor.getString(1);
                date = cursor.getString(2);

                photoList.add(new PhotoModel(Photo_Id, Photo_Path, date));

                cursor.moveToNext();
            }
        }

        cursor.close();

        return photoList;
    }

    public PhotoModel getPhotoByPosition(int position) {
        PhotoModel photoModel = null;

        String Photo_Id;
        String Photo_Path;
        String date;

        Cursor cursor = context.getContentResolver().query(PhotoContract.Photo.CONTENT_URI,
                PhotoContract.Photo.PROJECTION_ALL, null, null,
                PhotoContract.Photo._ID + " ASC");

        if (cursor != null) {
            cursor.moveToPosition(position);
            Photo_Id = cursor.getString(0);
            Photo_Path = cursor.getString(1);
            date = cursor.getString(2);

            photoModel = new PhotoModel(Photo_Id, Photo_Path, date);
        }
        cursor.close();
        return photoModel;
    }
}
