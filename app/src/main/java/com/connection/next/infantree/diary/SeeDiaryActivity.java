package com.connection.next.infantree.diary;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.connection.next.infantree.R;
import com.connection.next.infantree.db.ProviderDBHelper;
import com.connection.next.infantree.network.ImageDownloadHelper;
import com.connection.next.infantree.network.Proxy;
import com.connection.next.infantree.provider.InfantreeContract;
import com.connection.next.infantree.util.ServerUrls;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeeDiaryActivity extends ActionBarActivity implements View.OnClickListener {

    private String currentDiaryDate;

    private ImageView imageView;

    private RelativeLayout momLayout;
    private TextView momDiaryTextView;
    private Button momEditButton;
    private Button momOkayButton;
    private Button momCancelButton;
    private CircleImageView momCircleImage;

    private RelativeLayout dadLayout;
    private TextView dadDiaryTextView;
    private Button dadEditButton;
    private CircleImageView dadCircleImage;

    private ViewSwitcher textSwitcher;
    private ViewSwitcher buttonSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_diary);

//        String originalDate = getIntent().getStringExtra("date");
        String originalDate = "2015-05-15";
        currentDiaryDate = originalDate;
//        currentDiaryDate = (originalDate.split(" "))[0]; // "2015/05/15"
//        currentDiaryDate = originalDate; // "2015/05/15"
        final String convertedDate = currentDiaryDate; // "2015-05-15"
//        final String convertedDate = originalDate; // "2015-05-15"


        String where = "Diary_Id = ?";
        String[] whereParams = new String[]{currentDiaryDate};


        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                String json = Proxy.getJSON(ServerUrls.getDiaryByDate + convertedDate);
                new ProviderDBHelper(SeeDiaryActivity.this).insertDiaryJsonData(json);
            }
        };
        t.start();

        try {
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Cursor c = getContentResolver().query(InfantreeContract.Diaries.CONTENT_URI, null, where, whereParams, null);

        String photoId = null;
        String momDiary = null;
        String dadDiary = null;
        if (c != null) {
            // db에서 데이터 가져오기
            c.moveToFirst();
            photoId = c.getString(c.getColumnIndex(InfantreeContract.Diaries.PHOTO_ID));
            momDiary = c.getString(c.getColumnIndex(InfantreeContract.Diaries.MOM_DIARY));
            dadDiary = c.getString(c.getColumnIndex(InfantreeContract.Diaries.DAD_DIARY));
        }
        imageView = (ImageView) findViewById(R.id.see_diary_image_view);

        momLayout = (RelativeLayout) findViewById(R.id.see_diary_mom_diary_layout);
        dadLayout = (RelativeLayout) findViewById(R.id.see_diary_dad_diary_layout);

        // 일단 엄마 것부터
        momDiaryTextView = (TextView) momLayout.findViewById(R.id.see_diary_text_view);
        momEditButton = (Button) momLayout.findViewById(R.id.see_diary_edit_button);
        momEditButton.setOnClickListener(this);
        momOkayButton = (Button) momLayout.findViewById(R.id.see_diary_okay_button);
        momOkayButton.setOnClickListener(this);
        momCancelButton = (Button) momLayout.findViewById(R.id.see_diary_cancel_button);
        momCancelButton.setOnClickListener(this);
        momCircleImage = (CircleImageView) momLayout.findViewById(R.id.see_diary_circle_image);

        textSwitcher = (ViewSwitcher) momLayout.findViewById(R.id.see_diary_text_switcher);
        buttonSwitcher = (ViewSwitcher) momLayout.findViewById(R.id.see_diary_button_switcher);

        dadDiaryTextView = (TextView) dadLayout.findViewById(R.id.see_diary_text_view);
        dadEditButton = (Button) dadLayout.findViewById(R.id.see_diary_edit_button);
        dadEditButton.setVisibility(View.INVISIBLE); // 공간은 차지해야 안 밀리고 예쁘게 나옴
        dadCircleImage = (CircleImageView) dadLayout.findViewById(R.id.see_diary_circle_image);

        momDiaryTextView.setText(momDiary);
        dadDiaryTextView.setText(dadDiary);

        // 이미지 설정 - photoid 없으면, 오늘의 사진 설정 안 되있으면 아무것도 안해줘도 됨
        final String photo_Id = photoId;
        if (photo_Id != null) {
            final String imagePath = this.getFilesDir().getPath() + "/" + photoId;
            File imageFilePath = new File(imagePath);
            if (!imageFilePath.exists()) {
                Log.e("huhuhuh", "이미지 파일이 존재하지 않습니다!");
                Thread tt = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        new ImageDownloadHelper(SeeDiaryActivity.this).downloadImageFile("http://125.209.194.223:3000/image?_id=", photo_Id);
                    }

                };
                tt.start();

                try {
                    tt.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            WeakReference<ImageView> imageViewReference = new WeakReference<>(imageView);
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            imageViewReference.get().setImageBitmap(bm);
        }

        momCircleImage.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.mom));
        dadCircleImage.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.dad));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.see_diary_edit_button:
                buttonSwitcher.showNext();
                textSwitcher.showNext();
                ((EditText) textSwitcher.getCurrentView()).setText(momDiaryTextView.getText());
                break;

            case R.id.see_diary_okay_button:
                String afterEdit = ((EditText) textSwitcher.getCurrentView()).getText().toString();
                buttonSwitcher.showNext();
                textSwitcher.showNext();
                ((TextView) textSwitcher.getCurrentView()).setText(afterEdit); // textview 내용 바꿔주기

                // 엄만지 아빤지 확인해서 두번째 혹은 세번째 인자에 넣어주기
                Proxy.postDiary(currentDiaryDate, afterEdit, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(SeeDiaryActivity.this, "다이어리 업로드 성공!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(SeeDiaryActivity.this, "다이어리 업로드 실패!", Toast.LENGTH_LONG).show();
                    }
                });

                break;

            case R.id.see_diary_cancel_button:
                buttonSwitcher.showNext();
                textSwitcher.showNext();
                break;
        }
    }
}
