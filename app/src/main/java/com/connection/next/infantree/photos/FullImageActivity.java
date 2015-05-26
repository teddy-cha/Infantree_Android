package com.connection.next.infantree.photos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.connection.next.infantree.R;
import com.connection.next.infantree.home.HomeActivity;
import com.urqa.clientinterface.URQAController;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by chayongbin on 15. 5. 25..
 */
public class FullImageActivity extends Activity {

    PhotoViewAttacher mAttacher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        // get intent data
        Intent intent = getIntent();

        // Selected image id
        int position = intent.getExtras().getInt("id");
        String date = intent.getExtras().getString("date");

        AllPhotosAdapter imageAdapter = new AllPhotosAdapter(this, date);

        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        try {
            String img_path = HomeActivity.getAppContext().getFilesDir().getPath() + "/" + imageAdapter.photoListByDate.get(position);
            Bitmap bitmap = BitmapFactory.decodeFile(img_path);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            URQAController.SendException(e);
        }

        mAttacher = new PhotoViewAttacher(imageView);
    }

}
