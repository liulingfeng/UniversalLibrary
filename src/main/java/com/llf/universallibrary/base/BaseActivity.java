package com.llf.universallibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import com.llf.universallibrary.tools.AppManager;
import com.llf.universallibrary.tools.DialogTools;
import butterknife.ButterKnife;

/**
 * Created by llf on 2016/9/29.
 * 基础的Activity
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetcontentView();
        setContentView(getLayoutId());

        ButterKnife.bind(this);
        this.initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
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

    public abstract int getLayoutId();
    public abstract void initView();
}
