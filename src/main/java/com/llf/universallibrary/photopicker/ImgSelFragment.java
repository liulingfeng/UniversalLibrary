package com.llf.universallibrary.photopicker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.llf.universallibrary.Constant;
import com.llf.universallibrary.R;
import com.llf.universallibrary.tools.FileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by llf on 2016/10/17.
 */

public class ImgSelFragment extends Fragment implements View.OnClickListener{
    private GridView mRvImageList;
    private Button mBtnAlbumSelected;
    private RelativeLayout mRlBottom;

    private ImgSelConfig config;
    private ImageSelCallback callback;
    private List<Image> imageList = new ArrayList<>();
    private List<Folder> folderList = new ArrayList<>();
    private GalleryAdapter imageListAdapter;
    private FolderListAdapter mFolderListAdapter;
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    private static final int REQUEST_CAMERA = 5;
    private boolean hasFolderGened = false;
    private File tempFile;
    private ListPopupWindow folderPopupWindow;

    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID};
    public static ImgSelFragment instance(ImgSelConfig config) {
        ImgSelFragment fragment = new ImgSelFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_img_sel, container, false);
        mRvImageList = (GridView)view.findViewById(R.id.rvImageList);
        mBtnAlbumSelected = (Button)view.findViewById(R.id.btnAlbumSelected);
        mRlBottom = (RelativeLayout) view.findViewById(R.id.rlBottom);
        mBtnAlbumSelected.setOnClickListener(this);
        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        config = Constant.config;
        callback = (ImageSelCallback) getActivity();
        if (config.needCamera)
            imageList.add(new Image());
        imageListAdapter = new GalleryAdapter(getActivity(), imageList, config);
        mRvImageList.setAdapter(imageListAdapter);
        mRvImageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (config.needCamera && position == 0) {
                    showCameraAction();
                } else {
                    if (config.multiSelect) {
                        if (Constant.imageList.contains(imageList.get(position).path)) {
                            Constant.imageList.remove(imageList.get(position).path);
                            if (callback != null) {
                                callback.onImageUnselected(imageList.get(position).path);
                            }
                        } else {
                            if (config.maxNum <= Constant.imageList.size()) {
                                Toast.makeText(getActivity(), "最多选择" + config.maxNum + "张图片", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Constant.imageList.add(imageList.get(position).path);
                            if (callback != null) {
                                callback.onImageSelected(imageList.get(position).path);
                            }
                        }
                        imageListAdapter.select(imageList.get(position));
                    } else {
                        if (callback != null) {
                            callback.onSingleImageSelected(imageList.get(position).path);
                        }
                    }
                }
            }
        });

        mFolderListAdapter = new FolderListAdapter(getActivity(), folderList);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                folderList.add(new Folder());
                int count = data.getCount();
                if (count > 0) {
                    List<Image> tempImageList = new ArrayList<>();
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        if (!image.path.endsWith("gif"))
                            tempImageList.add(image);
                        if (!hasFolderGened) {
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            Folder folder = new Folder();
                            folder.name = folderFile.getName();
                            folder.path = folderFile.getAbsolutePath();
                            folder.cover = image;
                            if (!folderList.contains(folder)) {
                                List<Image> imageList = new ArrayList<>();
                                imageList.add(image);
                                folder.images = imageList;
                                folderList.add(folder);
                            } else {
                                Folder f = folderList.get(folderList.indexOf(folder));
                                f.images.add(image);
                            }
                        }

                    } while (data.moveToNext());

                    imageList.clear();
                    if (config.needCamera)
                        imageList.add(new Image());
                    imageList.addAll(tempImageList);
                    imageListAdapter.notifyDataSetChanged();
                    mFolderListAdapter.notifyDataSetChanged();
                    hasFolderGened = true;
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private void createPopupFolderList(int width, int height) {
        folderPopupWindow = new ListPopupWindow(getActivity());
        folderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        folderPopupWindow.setAdapter(mFolderListAdapter);
        folderPopupWindow.setContentWidth(width);
        folderPopupWindow.setWidth(width);
        folderPopupWindow.setHeight(height);
        folderPopupWindow.setAnchorView(mRlBottom);
        folderPopupWindow.setModal(true);
        folderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                folderPopupWindow.dismiss();
                mFolderListAdapter.setSelectIndex(position);
                if (position == 0) {
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                    mBtnAlbumSelected.setText("所有图片");
                } else {
                    imageList.clear();
                    if (config.needCamera)
                        imageList.add(new Image());
                    imageList.addAll(folderList.get(position).images);
                    imageListAdapter.notifyDataSetChanged();
                    mBtnAlbumSelected.setText(folderList.get(position).name);
                }
            }
        });
    }

    private void showCameraAction() {
        if (config.maxNum <= Constant.imageList.size()) {
            Toast.makeText(getActivity(), "最多选择" + config.maxNum + "张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            tempFile = new File(FileUtils.createRootPath(getActivity()) + "/" + System.currentTimeMillis() + ".jpg");
            FileUtils.createFile(tempFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(getActivity(), "打开相机失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (tempFile != null) {
                    if (callback != null) {
                        callback.onCameraShot(tempFile);
                    }
                }
            } else {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if(view == mBtnAlbumSelected){
            if (folderPopupWindow == null) {
                WindowManager wm = getActivity().getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                createPopupFolderList(width / 3 * 2, width / 3 * 2);
            }

            if (folderPopupWindow.isShowing()) {
                folderPopupWindow.dismiss();
            } else {
                folderPopupWindow.show();
                int index = mFolderListAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                folderPopupWindow.getListView().setSelection(index);
            }
        }
    }
}
