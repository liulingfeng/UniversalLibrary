package com.llf.universallibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import com.llf.universallibrary.tools.AppManager;
import com.llf.universallibrary.tools.DialogTools;
import com.llf.universallibrary.widget.SwipeBackLayout;

import butterknife.ButterKnife;

/**
 * Created by llf on 2016/9/29.
 * 基础的Activity,支持滑动退出
 */

public abstract class BaseActivity extends AppCompatActivity implements SwipeBackLayout.OnFinishScroll{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetcontentView();
        setContentView(getLayoutId());

        ButterKnife.bind(this);
        this.initView();
    }

    private void doBeforeSetcontentView() {
        AppManager.getAppManager().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 通过Class 跳转
     */
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 有回调的跳转
     */
    public void startActivityForResult(Class<?> cls,int requestCode){
        startActivityForResult(cls, null, requestCode);
    }
    /**
     * 含有Bundle通过Class回调跳转
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void startProgressDialog(){
        DialogTools.showWaittingDialog(this);
    }

    public void closeProgressDialog(){
        DialogTools.closeWaittingDialog();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void complete() {
        finish();
    }

    public abstract int getLayoutId();
    public abstract void initView();
}
