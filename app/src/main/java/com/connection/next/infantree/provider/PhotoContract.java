package com.connection.next.infantree.provider;

import android.net.Uri;
import android.provider.BaseColumns;

import com.connection.next.infantree.model.PhotoModel;

/**
 * Created by chayongbin on 15. 4. 28..
 */
public final class PhotoContract {
    public static final String AUTHORITY =
            "com.connection.next.infantree.provider";
   public static final Uri CONTENT_URI =
           Uri.parse("content://" + AUTHORITY);

   public static final class Photos implements BaseColumns {
       public static final String _ID = "_id";
       public static final String DATE = "date";

       public static final Uri CONTENT_URI = Uri.withAppendedPath(
               PhotoContract.CONTENT_URI, PhotoModel.class.getSimpleName()
       );

       public static final String[] PROJECTION_ALL = {
               _ID, DATE
       };

       //기본적인 정렬은 DATE를 기준으로 하도록 한다. 방식은 DESC
       public static final String SORT_ORDER_DEFAULT = DATE + "_DESC";

   }
}
