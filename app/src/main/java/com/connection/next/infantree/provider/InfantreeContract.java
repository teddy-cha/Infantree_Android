package com.connection.next.infantree.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by chayongbin on 15. 4. 15..
 */
public final class InfantreeContract {

    public static final String AUTHORITY = "com.connection.next.infantree.provider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Diaries implements BaseColumns {
        public static final String DIARY_ID = "Diary_Id";
        public static final String PHOTO_ID = "Photo_Id";
        public static final String MOM_DIARY = "Mom_Diary";
        public static final String DAD_DIARY = "Dad_Diary";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                InfantreeContract.CONTENT_URI, Diaries.class.getSimpleName()
        );
        public static final String[] PROJECTION_ALL = {
                DIARY_ID, PHOTO_ID, MOM_DIARY, DAD_DIARY
        };

        public static final String SORT_ORDER_DEFAULT = DIARY_ID + " ASC";
    }
}
