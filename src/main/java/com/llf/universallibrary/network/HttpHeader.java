package com.llf.universallibrary.network;

import com.llf.universallibrary.Constant;

import okhttp3.FormBody;

/**
 * Created by llf on 2016/10/14.
 * 公共的信息
 */

public class HttpHeader {
    protected static FormBody.Builder initFormEncodingBuilder(){
        FormBody.Builder body=new FormBody.Builder();
        //这里设置一些基础的参数
        return body;
    }

    protected static String initHttpUrl(String method) {
        return Constant.NETWORK_URL  + method;
    }
}
