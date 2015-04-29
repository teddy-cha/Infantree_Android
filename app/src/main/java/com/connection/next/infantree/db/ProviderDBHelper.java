package com.connection.next.infantree.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.connection.next.infantree.model.PhotoModel;
import com.connection.next.infantree.network.ImageDownloadHelper;
import com.connection.next.infantree.provider.PhotoContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by chayongbin on 15. 4. 15..
 */
public class ProviderDBHelper {
    private Context context;
    private SQLiteDatabase database;

    public ProviderDBHelper(Context context) {
        this.context = context;

        database = context.openOrCreateDatabase("babyPhoto.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        try {
            String sql = "CREATE TABLE IF NOT EXISTS Photos " +
                    "(PhotoNum integer primary key autoincrement, " +
                    "_id text unique not null, " +
                    "date datetime not null); ";
            database.execSQL(sql);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertJsonData(String jsonData) {
        String _id;
        String date;

        ImageDownloadHelper imageDownloadHelper = new ImageDownloadHelper(context);

        try {
            JSONArray jsonArray = new JSONArray(jsonData);

            for (int i=0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                _id = jsonObject.getString("_id");
                date = jsonObject.getString("date");

                java.sql.Date sqlDate = null;
                java.util.Date utilDate;

                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                try {
                    utilDate = transFormat.parse(date);
                    sqlDate = new java.sql.Date(utilDate.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.i("json is " + date, _id);


//                String sql = "INSERT INTO Photos(_id, date) values('" + _id + "', '" + sqlDate + "');";
//
//                try {
//                    database.execSQL(sql);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                ContentValues values = new ContentValues();

                values.put("_id", _id);
                values.put("date", date);

                context.getContentResolver().insert(PhotoContract.Photos.CONTENT_URI, values);


                imageDownloadHelper.downloadImageFile("http://125.209.194.223:3000/image?_id=" + _id, _id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getDateCounts() {

//        String sql = "SELECT date FROM Photos GROUP BY date;";
//        Cursor cursor = database.rawQuery(sql,null);

        Cursor cursor = context.getContentResolver().query(
                PhotoContract.Photos.CONTENT_URI,
                PhotoContract.Photos.PROJECTION_ALL,
                null, null,
                PhotoContract.Photos.DATE + " GROUP BY date"
        );

        cursor.moveToNext();
        int count = cursor.getCount();
        System.out.println("Date Count is " + count);

        cursor.close();
        return count;
    }

    public ArrayList<PhotoModel> getPhotoList() {
        ArrayList<PhotoModel> photoList = new ArrayList<>();

        String _id;
        String date;

        String sql = "SELECT * FROM Photos GROUP BY date ORDER BY PhotoNum DESC;";
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            _id = cursor.getString(1);
            date = cursor.getString(2);
            System.out.println("Date is " + date);
            photoList.add(new PhotoModel(_id, date));
        }
        cursor.close();
        return photoList;
    }

    public ArrayList<String> getPhotoListByDate(String date) {
        ArrayList<String> photoList = new ArrayList<>();

        String _id;

        String sql = "SELECT * FROM Photos WHERE date = '" + date + "' ORDER BY PhotoNum DESC LIMIT 3;";
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            _id = cursor.getString(1);
            photoList.add(_id);
        }
        cursor.close();
        return photoList;
    }

}
