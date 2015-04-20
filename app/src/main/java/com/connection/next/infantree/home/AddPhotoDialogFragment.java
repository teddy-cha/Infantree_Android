package com.connection.next.infantree.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.connection.next.infantree.R;

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
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_CAMERA_IMAGE);
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

        getDialog().dismiss();
    }

    // TODO: 여기 완성할 것!
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA_IMAGE:

                    break;
                case REQUEST_GALLERY_IMAGE:

                    break;
            }
        } else {
            Toast.makeText(getActivity(), "[onActivityResult] 오류가 발생했습니다!", Toast.LENGTH_SHORT).show();
        }
    }
}