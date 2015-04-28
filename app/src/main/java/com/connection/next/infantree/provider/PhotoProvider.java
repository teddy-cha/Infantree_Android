package com.connection.next.infantree.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.Locale;

/**
 * Created by chayongbin on 15. 4. 28..
 */
public class PhotoProvider extends ContentProvider {

    private Context context;
    private SQLiteDatabase database;
    private final String TABLE_NAME = "Photos";

   //query와 insert에서 사용할 URI정보를 관리할 URI MATCHER 객체를 생성합니다.
    private static final int PHOTO_LIST = 1;
    private static final int PHOTO_ID = 2;
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher((UriMatcher.NO_MATCH));
        URI_MATCHER.addURI(PhotoContract.AUTHORITY, "Photos", PHOTO_LIST);
        URI_MATCHER.addURI(PhotoContract.AUTHORITY, "Photos/#", PHOTO_ID);
    }

    private void sqLiteInitialize() {
        database = context.openOrCreateDatabase("Infantree.db",
                SQLiteDatabase.CREATE_IF_NECESSARY, null);
        database.setLocale(Locale.getDefault());
        database.setVersion(1);
    }

    private void tableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS Photos " +
                "(PhotoNum integer primary key autoincrement, " +
                "_id text unique not null, " +
                "date datetime not null); ";
        database.execSQL(sql);
    }

    @Override
    public boolean onCreate() {
        this.context = getContext();
        sqLiteInitialize();
        tableCreate();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (URI_MATCHER.match(uri) != PHOTO_LIST) {
            throw new IllegalArgumentException("Insertion을 지원하지 않는 URI입니다. URI : " + uri);
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
