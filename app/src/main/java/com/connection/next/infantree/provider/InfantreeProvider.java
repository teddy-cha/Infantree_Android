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
public class InfantreeProvider extends ContentProvider {

    private Context context;
    private SQLiteDatabase database;
    private final String PHOTO_TABLE_NAME = "Photos";
    private final String DIARY_TABLE_NAME = "Diaries";

    private static final int PHOTO_LIST = 1;
    private static final int PHOTO_ID = 2;
    private static final int DIARY_LIST = 3;
    private static final int DIARY_ID = 4;
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(InfantreeContract.AUTHORITY, "Diaries", DIARY_LIST);
        URI_MATCHER.addURI(InfantreeContract.AUTHORITY, "Diaries/#", DIARY_ID);
    }

    private void sqLiteInitialize() {
        database = context.openOrCreateDatabase("testdb.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        database.setLocale(Locale.getDefault());
        database.setVersion(1);
    }

    private void tableCreate() {
        String diary_sql = "CREATE TABLE IF NOT EXISTS "
                + DIARY_TABLE_NAME
                + "(Diary_Id text primary key,"
                + "Photo_Id text,"
                + "Mom_Diary text,"
                + "Dad_Diary text);";
        database.execSQL(diary_sql);
    }

    @Override
    public boolean onCreate() {
        this.context = getContext();
        sqLiteInitialize();
        tableCreate();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String whichTable;
        String[] whichProjection;

        switch (URI_MATCHER.match(uri)) {

            case DIARY_LIST:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = InfantreeContract.Diaries.SORT_ORDER_DEFAULT;
                }
                whichTable = DIARY_TABLE_NAME;
                whichProjection = InfantreeContract.Diaries.PROJECTION_ALL;
                break;

            case DIARY_ID:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = InfantreeContract.Diaries.SORT_ORDER_DEFAULT;
                }
                if (selection == null) {
                    selection = "Diary_Id = ?";
                    selectionArgs = new String[]{uri.getLastPathSegment()};
                }
                whichTable = DIARY_TABLE_NAME;
                whichProjection = InfantreeContract.Diaries.PROJECTION_ALL;
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = database.query(whichTable, whichProjection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long resultId;
        Uri resultUri = null;

        switch (URI_MATCHER.match(uri)) {
            case DIARY_LIST:
                resultId = database.insertWithOnConflict(DIARY_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE); // id 일치할 경우 기존것 지우고 이걸로 대체
                resultUri = ContentUris.withAppendedId(uri, resultId);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
