package com.llf.universallibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.llf.universallibrary.tools.DialogTools;

import butterknife.ButterKnife;

/**
 * Created by llf on 2016/9/30.
 * 基础Fragment
 */

public abstract class BaseFragment extends Fragment {
    protected View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        if(rootView == null)
            rootView = inflater.inflate(getLayoutResource(),container,false);
        ButterKnife.bind(this,rootView);
        initView();
        return rootView;
    }

    //获取布局
    protected abstract int getLayoutResource();
    //初始化布局
    protected abstract void initView();

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 开启加载进度条
     */
    public void startProgressDialog() {
        DialogTools.showWaittingDialog(getActivity());
    }

    /**
     * 停止加载进度条
     */
    public void stopProgressDialog() {
        DialogTools.closeWaittingDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
