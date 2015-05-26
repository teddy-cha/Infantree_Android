package com.connection.next.infantree.diary;

import android.content.SharedPreferences;
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
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeeDiaryActivity extends ActionBarActivity implements View.OnClickListener {

    private String babyId;
    private String parent;
    private String serverUrl;
    private String currentDiaryDate;

    private ViewSwitcher textSwitcher;
    private ViewSwitcher buttonSwitcher;
    private TextView currentParentDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_diary);

        // shared preferences에서 값들 가져온다
        SharedPreferences pref = getSharedPreferences(getResources().getString(R.string.pref_name), MODE_PRIVATE);
        babyId = pref.getString(getResources().getString(R.string.baby_id), "");
        parent = pref.getString(getResources().getString(R.string.parent), "");
        serverUrl = pref.getString(getResources().getString(R.string.server_url), "");

        // 인텐트에서 date 값 가져오고 형식에 맞게 수정
        final String originalDate = getIntent().getStringExtra("date"); // "2015-05-15"
        currentDiaryDate = originalDate.replace('-', '/'); // "2015/05/15"

        // 서버에서 다이어리 데이터 가져와서 DB에 넣어준다
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                String request = serverUrl + "diary?baby_id=" + babyId + "&date=" + originalDate;
                if (request != null) {
                    String json = Proxy.getJSON(request);
                    new ProviderDBHelper(SeeDiaryActivity.this).insertDiaryJsonData(json);
                } else {
                    Log.e("huhuhu", "currently no diary is on the server!");
                }
            }
        };
        t.start();

        // 네트워크 작업 완료될 때까지 기다린다
        try {
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 내부 DB에서 다이어리 데이터 가져온다
        String where = "Diary_Id = ?";
        String[] whereParams = new String[]{currentDiaryDate};
        Cursor c = getContentResolver().query(InfantreeContract.Diaries.CONTENT_URI, null, where, whereParams, null);
        String photoId = null;
        String momDiary = null;
        String dadDiary = null;
        if (c.getCount() != 0) {
            c.moveToFirst();
            photoId = c.getString(c.getColumnIndex(InfantreeContract.Diaries.PHOTO_ID));
            momDiary = c.getString(c.getColumnIndex(InfantreeContract.Diaries.MOM_DIARY));
            dadDiary = c.getString(c.getColumnIndex(InfantreeContract.Diaries.DAD_DIARY));
        }

        // 이미지뷰에 그림 넣어주고 텍스트 설정해준다
        ImageView imageView = (ImageView) findViewById(R.id.see_diary_image_view);
        TextView todaySelectText = (TextView) findViewById(R.id.today_text);

        // 이미지가 있으면 바로 가져오고 없으면 서버에서 다운 받아서 가져온다
        if (photoId != null) {
            todaySelectText.setText(""); // 사진이 있으면 빈 내용
            final String imagePath = this.getFilesDir().getPath() + "/" + photoId;
            File imageFilePath = new File(imagePath);
            if (!imageFilePath.exists()) {
                Log.e("huhuhuh", "이미지 파일이 존재하지 않습니다!");
                final String photoIdd = photoId;
                Thread tt = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        new ImageDownloadHelper(SeeDiaryActivity.this).downloadImageFile("http://125.209.194.223:3000/image?_id=", photoIdd);
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
        // 오늘의 사진 설정 안 되어 있을 경우
        else {
            todaySelectText.setText("오늘의 사진을 등록해주세요");
            imageView.setImageResource(R.drawable.todayphoto);
        }

        // 엄마 뷰
        RelativeLayout momLayout = (RelativeLayout) findViewById(R.id.see_diary_mom_diary_layout);
        TextView momDiaryTextView = (TextView) momLayout.findViewById(R.id.see_diary_text_view);
        Button momEditButton = (Button) momLayout.findViewById(R.id.see_diary_edit_button);
        Button momOkayButton = (Button) momLayout.findViewById(R.id.see_diary_okay_button);
        Button momCancelButton = (Button) momLayout.findViewById(R.id.see_diary_cancel_button);
        CircleImageView momCircleImage = (CircleImageView) momLayout.findViewById(R.id.see_diary_circle_image);
        ViewSwitcher momTextSwitcher = (ViewSwitcher) momLayout.findViewById(R.id.see_diary_text_switcher);
        ViewSwitcher momButtonSwitcher = (ViewSwitcher) momLayout.findViewById(R.id.see_diary_button_switcher);

        // 아빠 뷰
        RelativeLayout dadLayout = (RelativeLayout) findViewById(R.id.see_diary_dad_diary_layout);
        TextView dadDiaryTextView = (TextView) dadLayout.findViewById(R.id.see_diary_text_view);
        Button dadEditButton = (Button) dadLayout.findViewById(R.id.see_diary_edit_button);
        Button dadOkayButton = (Button) dadLayout.findViewById(R.id.see_diary_okay_button);
        Button dadCancelButton = (Button) dadLayout.findViewById(R.id.see_diary_cancel_button);
        CircleImageView dadCircleImage = (CircleImageView) dadLayout.findViewById(R.id.see_diary_circle_image);
        ViewSwitcher dadTextSwitcher = (ViewSwitcher) dadLayout.findViewById(R.id.see_diary_text_switcher);
        ViewSwitcher dadButtonSwitcher = (ViewSwitcher) dadLayout.findViewById(R.id.see_diary_button_switcher);

        // 엄마일때
        if (parent.equals("mom")) {
            dadEditButton.setVisibility(View.INVISIBLE); // 공간은 차지해야 안 밀리고 예쁘게 나옴
            textSwitcher = momTextSwitcher;
            buttonSwitcher = momButtonSwitcher;
            currentParentDiary = momDiaryTextView;
            momEditButton.setOnClickListener(this);
            momOkayButton.setOnClickListener(this);
            momCancelButton.setOnClickListener(this);
        }
        // 아빠일때
        else if (parent.equals("dad")) {
            momEditButton.setVisibility(View.INVISIBLE);
            textSwitcher = dadTextSwitcher;
            buttonSwitcher = dadButtonSwitcher;
            currentParentDiary = dadDiaryTextView;
            dadEditButton.setOnClickListener(this);
            dadOkayButton.setOnClickListener(this);
            dadCancelButton.setOnClickListener(this);
        }
        // 설정안되어있을때
        else {
            Log.e("huhuhu", "please set shared preference parent key-value");
        }

        // 동그라미 프로필 사진 보여주기
        momCircleImage.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.mom));
        dadCircleImage.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.dad));

        // db로부터 불러온 다이어리 텍스트뷰에 보여주기
        momDiaryTextView.setText(momDiary);
        dadDiaryTextView.setText(dadDiary);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.see_diary_edit_button:
                buttonSwitcher.showNext();
                textSwitcher.showNext();
                ((EditText) textSwitcher.getCurrentView()).setText(currentParentDiary.getText());
                break;

            case R.id.see_diary_okay_button:
                final String afterEdit = ((EditText) textSwitcher.getCurrentView()).getText().toString();
                buttonSwitcher.showNext();
                textSwitcher.showNext();
                ((TextView) textSwitcher.getCurrentView()).setText(afterEdit); // textview 내용 바꿔주기

                // 엄만지 아빤지 확인해서 두번째 혹은 세번째 인자에 넣어주기
                Proxy.postDiary(serverUrl + "diary", babyId, currentDiaryDate, parent, afterEdit, new AsyncHttpResponseHandler() {
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
