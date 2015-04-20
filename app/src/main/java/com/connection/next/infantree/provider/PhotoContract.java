package com.connection.next.infantree.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by chayongbin on 15. 4. 15..
 */
public final class PhotoContract {

    public static final String AUTHORITY = "com.connection.next.infantree.provider";

    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public static final class Photo implements BaseColumns {
        public static final String PHOTO_ID = "Photo_Id";
        public static final String PHOTO_PATH = "Photo_Path";
        public static final String DATE = "Date";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                PhotoContract.CONTENT_URI, Photo.class.getSimpleName()
        );
        public static final String[] PROJECTION_ALL = {
                 PHOTO_ID, PHOTO_PATH, DATE
        };

        public static final String SORT_ORDER_DEFAULT =  PHOTO_ID + "ASC";

    }
}
