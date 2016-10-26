package com.llf.universallibrary.photopicker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.llf.universallibrary.Constant;
import com.llf.universallibrary.R;
import com.llf.universallibrary.base.BaseActivity;
import com.llf.universallibrary.tools.FileUtils;
import com.llf.universallibrary.widget.NormalTitleBar;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by llf on 2016/10/18.
 * 图片选择
 */

public class ImgSelActivity extends BaseActivity implements ImageSelCallback{

    public static void startActivity(Activity activity, ImgSelConfig config, int RequestCode) {
        Intent intent = new Intent(activity, ImgSelActivity.class);
        Constant.config = config;
        activity.startActivityForResult(intent, RequestCode);
    }

    public static void startActivity(Fragment fragment, ImgSelConfig config, int RequestCode){
        Intent intent = new Intent(fragment.getActivity(), ImgSelActivity.class);
        Constant.config = config;
        fragment.startActivityForResult(intent, RequestCode);
    }

    private FrameLayout mContent;
    private NormalTitleBar mTitlebar;
    private TextView btnConfirm;

    private ImgSelConfig config;
    private ArrayList<String> result = new ArrayList<>();
    public static final String INTENT_RESULT = "result";
    private static final int IMAGE_CROP_CODE = 1;
    private static final int STORAGE_REQUEST_CODE = 1;
    private String cropImagePath;
    @Override
    public int getLayoutId() {
        return R.layout.activity_img_sel;
    }

    @Override
    public void initView() {
        mContent = (FrameLayout)findViewById(R.id.content);
        mTitlebar = (NormalTitleBar)findViewById(R.id.titlebar);
        btnConfirm = (TextView)findViewById(R.id.tv_right);
        mTitlebar.setTitleText("图片");
        mTitlebar.setRightTitleVisibility(true);
        mTitlebar.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.imageList != null && !Constant.imageList.isEmpty()) {
                    exit();
                }
            }
        });
        Constant.imageList.clear();
        config = Constant.config;

        /**
         * 6.0系统动态权限申请需要
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_CODE);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, ImgSelFragment.instance(config), null)
                    .commit();
        }
    }

    @Override
    public void onSingleImageSelected(String path) {
        if (config.needCrop) {
            crop(path);
        } else {
            Constant.imageList.add(path);
            exit();
        }
    }

    @Override
    public void onImageSelected(String path) {
        btnConfirm.setText("确定(" + Constant.imageList.size() + "/" + config.maxNum + ")");
    }

    @Override
    public void onImageUnselected(String path) {
        btnConfirm.setText("确定(" + Constant.imageList.size() + "/" + config.maxNum + ")");
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            if (config.needCrop) {
                crop(imageFile.getAbsolutePath());
            } else {
                Constant.imageList.add(imageFile.getAbsolutePath());
                exit();
            }
        }
    }

    private void crop(String imagePath) {
        if (!FileUtils.isSdCardAvailable()) {
            Toast.makeText(this, "SD卡不可用", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(FileUtils.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg");
        cropImagePath = file.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(imagePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", config.aspectX);
        intent.putExtra("aspectY", config.aspectY);
        intent.putExtra("outputX", config.outputX);
        intent.putExtra("outputY", config.outputY);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, IMAGE_CROP_CODE);
    }

    public void exit() {
        Intent intent = new Intent();
        result.clear();
        result.addAll(Constant.imageList);
        intent.putStringArrayListExtra(INTENT_RESULT, result);
        setResult(RESULT_OK, intent);
        Constant.imageList.clear();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CROP_CODE && resultCode == RESULT_OK) {
            Constant.imageList.add(cropImagePath);
            exit();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case STORAGE_REQUEST_CODE:
                if(grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.content, ImgSelFragment.instance(config), null)
                            .commit();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }
    }
}
