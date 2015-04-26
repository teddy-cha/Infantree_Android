package com.connection.next.infantree.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by chayongbin on 15. 4. 15..
 * 데이터베이스롸 직접 연결하는 부분
 */
public class PhotoProvider extends ContentProvider {

    private Context context;
    private SQLiteDatabase database;
    private final String TABLE_NAME = "Photos";

    /*
     * query와 insert에서 사용할 URI정보 관리하는 Uri Matcher 객체 생성
     */

    private static final int PHOTO_LIST = 1;
    private static final int PHOTO_ID = 2;
    private static final UriMatcher URI_MATCHER;

    static  {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PhotoContract.AUTHORITY, "Photos", PHOTO_LIST);
        URI_MATCHER.addURI(PhotoContract.AUTHORITY, "Photos/#", PHOTO_ID);
    }

    private void sqLiteInitalize() {
        /*
         * 1차적으로 testdb로 일시적으로 테스트를 하자...
         */
        database = context.openOrCreateDatabase("testdb.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        database.setLocale(Locale.getDefault());
        database.setVersion(1);
    }

    private void tableCreate() {
        String photo_sql = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME
                + "(Photo_Id integer primary key autoincrement,"
                + "Photo_Path text not null,"
                + "date date not null);";
        database.execSQL(photo_sql);
    }

//    private boolean isTalbleExist() {
//        String searchTable = "select distinct tbl_name from" +
//                "sqlite master where tbl_name = '" + TABLE_NAME + "';";
//        Cursor cursor = database.rawQuery(searchTable, null);
//
//        if(cursor.getCount() == 0) {
//            return false;
//        }
//        cursor.close();
//        return true;
//    }

    @Override
    public boolean onCreate() {
        this.context = getContext();
        sqLiteInitalize();
//        if (!isTalbleExist()) {
            tableCreate();
//        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (URI_MATCHER.match(uri)) {
            case PHOTO_LIST:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = PhotoContract.Photo.SORT_ORDER_DEFAULT;
                }
                break;
            case PHOTO_ID:
                if(TextUtils.isEmpty(sortOrder)) {
                    sortOrder = PhotoContract.Photo.SORT_ORDER_DEFAULT;
                }
                if(selection == null) {
                    selection = "PHOTO_ID = ?";
                    selectionArgs = new String[] {uri.getLastPathSegment()};
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI : " + uri);
        }

        Cursor cursor = database.query(TABLE_NAME, PhotoContract.Photo.PROJECTION_ALL, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (URI_MATCHER.match(uri) != PHOTO_LIST) {
            throw new IllegalArgumentException("Insertion을 지원하지 않습니다. : " + uri );
        }

        if (URI_MATCHER.match(uri) == PHOTO_LIST) {
            long id = database.insert("Photos", null, values);

            Uri itemUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(itemUri, null);

            return itemUri;
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
