package com.connection.next.infantree.photos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.connection.next.infantree.R;
import com.connection.next.infantree.db.PhotoDBHelper;
import com.connection.next.infantree.home.HomeActivity;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by chayongbin on 15. 5. 25..
 */
public class AllPhotosAdapter extends BaseAdapter{

    private Context context;
    public ArrayList<String> photoListByDate;
    private String date;
    private PhotoDBHelper dao;

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.aa, R.drawable.bb, R.drawable.aa, R.drawable.aa, R.drawable.bb, R.drawable.aa
    };

    // Constructor
    public AllPhotosAdapter(Context context, String date){
        this.context = context;
        this.date = date;

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
        String img_path = HomeActivity.getAppContext().getFilesDir().getPath() + "/" + photoListByDate.get(position);

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





        return imageView;
    }



//    photoListByDate = dao.getPhotoListByDateOfThree(date);
//    System.out.println("photoListByDate : " + photoListByDate);
//    int size = photoListByDate.size();
//    holder.spannable.setAdapter(new SpannableAdapter(mContext, holder.spannable, date, size));
//    Log.i("Data is ",date);
}
