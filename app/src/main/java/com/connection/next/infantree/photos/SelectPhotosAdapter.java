package com.connection.next.infantree.photos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.connection.next.infantree.R;
import com.connection.next.infantree.db.PhotoDBHelper;
import com.connection.next.infantree.diary.SeeDiaryActivity;
import com.connection.next.infantree.home.HomeActivity;
import com.connection.next.infantree.network.Proxy;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by chayongbin on 15. 5. 25..
 */
public class SelectPhotosAdapter extends BaseAdapter{

    private Context context;
    public ArrayList<String> photoListByDate;
    private String date;
    private PhotoDBHelper dao;
    private String request, babyId, diaryDate;

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.aa, R.drawable.bb, R.drawable.aa, R.drawable.aa, R.drawable.bb, R.drawable.aa
    };

    // Constructor
    public SelectPhotosAdapter(Context context, String date, String request, String babyId, String diaryDate){
        this.context = context;
        this.date = date;
        this.request = request;
        this.babyId = babyId;
        this.diaryDate = diaryDate;

        dao = new PhotoDBHelper(HomeActivity.getAppContext());
        photoListByDate = dao.getPhotoListByDate(date);
    }

    @Override
    public int getCount() {
        return photoListByDate.size();
    }

    @Override
    public Object getItem(int position) {
        return photoListByDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        options.inPurgeable = true;
        final String img_path = HomeActivity.getAppContext().getFilesDir().getPath() + "/" + photoListByDate.get(position);
        final String img_id= photoListByDate.get(position);

        Bitmap bitmap = BitmapFactory.decodeFile(img_path, options);

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Bitmap resized = null;

        resized = Bitmap.createScaledBitmap(bitmap, (width*340) / height, 340, true);

//        resized = Bitmap.createScaledBitmap(bitmap, 340, 340, true);

        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(resized);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(340, 340));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Proxy.postTodayPhoto(request + "diary", babyId, diaryDate, img_id, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(context, "다이어리 업로드 성공!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, HomeActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(context, "다이어리 업로드 실패!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return imageView;
    }



//    photoListByDate = dao.getPhotoListByDateOfThree(date);
//    System.out.println("photoListByDate : " + photoListByDate);
//    int size = photoListByDate.size();
//    holder.spannable.setAdapter(new SpannableAdapter(mContext, holder.spannable, date, size));
//    Log.i("Data is ",date);
}
