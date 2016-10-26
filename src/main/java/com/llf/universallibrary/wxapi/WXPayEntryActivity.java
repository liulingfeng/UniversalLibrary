package com.llf.universallibrary.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import com.llf.universallibrary.Constant;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by llf on 2016/10/14.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
    private IWXAPI mIWXAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIWXAPI = WXAPIFactory.createWXAPI(this, Constant.AppID);
        mIWXAPI.handleIntent(getIntent(),this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    /**
     * 0成功
     * -1错误
     * -2用户取消
     * @param baseResp
     */
    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("标题");
            builder.setMessage(baseResp.errCode+"");
            builder.create().show();
        }
    }
}
