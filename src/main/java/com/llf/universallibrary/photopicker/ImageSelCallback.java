package com.llf.universallibrary.photopicker;

import java.io.File;

/**
 * Created by llf on 2016/10/17.
 * 选择图片后的回调
 */

public interface ImageSelCallback {
    void onSingleImageSelected(String path);

    void onImageSelected(String path);

    void onImageUnselected(String path);

    void onCameraShot(File imageFile);
}
