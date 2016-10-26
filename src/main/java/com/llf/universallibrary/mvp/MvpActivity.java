package com.llf.universallibrary.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.llf.universallibrary.tools.DialogTools;
import butterknife.ButterKnife;

/**
 * Created by llf on 2016/10/26.
 */

public abstract class MvpActivity <P extends BasePresenter> extends AppCompatActivity{
    protected P mvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        ButterKnife.bind(this);
        this.initView();
    }

    protected abstract P createPresenter();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }
}
