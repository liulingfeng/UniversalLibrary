package com.llf.universallibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import com.llf.universallibrary.Constant;
import com.llf.universallibrary.tools.AppManager;
import com.llf.universallibrary.tools.DialogTools;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import butterknife.ButterKnife;

/**
 * Created by llf on 2016/10/14.
 * 涉及到微信支付的Activity
 */

public abstract class WeixinBaseActivity extends AppCompatActivity {
    private IWXAPI mIWXAPI;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetcontentView();
        setContentView(getLayoutId());

        ButterKnife.bind(this);
        reqWeixin();
        this.initView();
    }

    private void reqWeixin(){
        mIWXAPI = WXAPIFactory.createWXAPI(this, Constant.AppID,true);
        mIWXAPI.registerApp(Constant.AppID);
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

    /**
     * 调起支付
     */
    public void weixinPay(){
        PayReq request = new PayReq();
        request.appId = Constant.AppID;
        request.partnerId = "1305176001";
        request.prepayId = "wx20161014191502b2ed9648fd0975793318";
        request.packageValue = "Sign=WXPay";
        request.nonceStr = "eb740908319fac25eedd5dc66b194897";
        request.timeStamp = "1476443702";
        request.sign = "83CD08775BE6E0CDEFD57080B0EC54C9";
        mIWXAPI.sendReq(request);
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
