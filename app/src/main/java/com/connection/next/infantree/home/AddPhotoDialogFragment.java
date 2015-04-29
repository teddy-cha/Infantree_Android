package com.connection.next.infantree.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.connection.next.infantree.R;
import com.connection.next.infantree.network.ImageUploadHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by viz on 15. 4. 20..
 */
public class AddPhotoDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final int REQUEST_CAMERA_IMAGE = 1;
    private static final int REQUEST_GALLERY_IMAGE = 2;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View customDialog = LayoutInflater.from(getActivity()).inflate(R.layout.add_button_dialog, null);
        builder.setView(customDialog); // 레이아웃으로 만들어서 따로

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.add_button_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.add_button_dialog_height);
        getDialog().getWindow().setLayout(width, height);

        // 꼭 생성된 Dialog View에서 버튼을 찾아줘야 된다 안 그럼 null point exception 남
        Button addFromCamera = (Button) getDialog().findViewById(R.id.add_from_camera);
        Button addFromGallery = (Button) getDialog().findViewById(R.id.add_from_gallery);
        addFromCamera.setOnClickListener(this);
        addFromGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_from_camera:
                if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
                    Toast.makeText(getActivity(), "기기에 카메라 기능이 없습니다!", Toast.LENGTH_SHORT).show();
                    break;
                }

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Ensure that there's a camera activity to handle the intent
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(cameraIntent, REQUEST_CAMERA_IMAGE);
                    }
                } else {
                    Toast.makeText(getActivity(), "카메라를 실행 가능한 앱이 없습니다!", Toast.LENGTH_SHORT).show();
                    break;
                }

                break;

            case R.id.add_from_gallery:
                // https://github.com/giljulio/android-multiple-image-picker 요거 쓸까?
                Intent galleryIntent = new Intent();
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_GALLERY_IMAGE);
                break;
        }
    }

    // TODO: 여기 완성할 것!
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA_IMAGE:
                    // 시작 전 intent에 특정 uri 넣어주면 data는 null값으로 반환된다
                    Log.e("testing", currentPhotoPath);
                    ArrayList<String> singleImagePathList1 = new ArrayList<String>();
                    singleImagePathList1.add(currentPhotoPath);
                    new ImageUploadHelper(getActivity()).uploadImageFile(singleImagePathList1);
                    break;
                case REQUEST_GALLERY_IMAGE:
                    // 파일 하나일 때는 그냥 data.getData()
                    if (data.getData() != null) {
                        Uri photoUri = data.getData();
                        Log.e("testing", photoUri.toString());

                        String filePath = readContentUriToFile(photoUri);
                        ArrayList<String> singleImagePathList2 = new ArrayList<String>();
                        singleImagePathList2.add(filePath);
                        new ImageUploadHelper(getActivity()).uploadImageFile(singleImagePathList2);
                    } else {
                        // 파일 두 개 이상일 때는 data.getClipData()로 가져옴
                        ArrayList<String> multipleImagePathList = new ArrayList<String>();

                        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                            String filePath = readContentUriToFile(data.getClipData().getItemAt(i).getUri());
                            multipleImagePathList.add(filePath);
                        }

                        new ImageUploadHelper(getActivity()).uploadImageFile(multipleImagePathList);
                    }

                    break;
            }
        } else {
            Toast.makeText(getActivity(), "[onActivityResult] 오류가 발생했습니다!", Toast.LENGTH_SHORT).show();
        }

        getDialog().dismiss();
        // onClick에서 dismiss를 해버리면 startActivity를 호출한 fragment가 닫히기 때문에
        // onActivityResult를 못 받아오는 것 같다. 따라서 반드시 onActivityResult에서 할 일을 다 처리한 다음에
        // dismiss를 해줘야 한다. 이것 때문에 한참 헤맸다...ㅠㅠ
    }

    private String currentPhotoPath; // TODO: 이부분 바꿀것

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // onActivityResult에서 data 대신 사용
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String readContentUriToFile(Uri uri) {
        String filePath = null;

        try {
            InputStream is = getActivity().getContentResolver().openInputStream(uri);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);

            File targetFile = createImageFile();
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);

            filePath = targetFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }
}