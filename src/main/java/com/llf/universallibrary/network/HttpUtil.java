package com.llf.universallibrary.network;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.llf.universallibrary.tools.APPLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by lxs on 2016/10/14.
 * 网络请求工具
 */

public class HttpUtil {
    private static final String TAG = "OkHttpClientManager";
    private static HttpUtil manager;
    private OkHttpClient okHttpClient;
    private Handler mDelivery;

    private HttpUtil(Context context) {
        File sdcache = context.getExternalCacheDir();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(logging);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            builder.cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        }
        okHttpClient = builder.build();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static HttpUtil getInstanse(Context context) {
        // 加同步锁
        synchronized (HttpUtil.class) {
            if (manager == null) {
                manager = new HttpUtil(context);
            }
        }
        return manager;
    }

    public void postAsynHttp(String method, FormBody.Builder bodyParams, StringCallback callback) {
        RequestBody formBody = bodyParams.build();
        Request request = new Request.Builder()
                .url(HttpHeader.initHttpUrl(method))
                .post(formBody)
                .build();
        deliveryResult(callback, request);
    }

    public void getAsynHttp(String url, StringCallback callback){
        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.method("GET",null);
        Request request = requestBuilder.build();
        deliveryResult(callback, request);
    }

    public void postAsynFile(String method,File file, StringCallback callback){
        Request request = new Request.Builder()
                .url(HttpHeader.initHttpUrl(method))
                .post(RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        deliveryResult(callback, request);
    }

    public void downAsynFile(String url,StringCallback callback){
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(new File("/sdcard/wangshu.jpg"));
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                } catch (IOException e) {
                    APPLog.d("出错"+e.getMessage());
                    e.printStackTrace();
                }

                APPLog.d("文件下载成功");
            }
        });
    }

    private void deliveryResult(final StringCallback callback, final Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(request,e,callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String content = response.body().string();
                    sendSuccessStringCallback(content, callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }

    private void sendFailedStringCallback(final Request request, final IOException e, final StringCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onFailure(request, e);
            }
        });
    }

    private void sendSuccessStringCallback(final String content, final StringCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null){
                    Object result = JsonUtil.getObject(content,Object.class);
                    callback.onResponse(result);
                }
            }
        });
    }

    public interface StringCallback {
        void onFailure(Request request, IOException e);
        void onResponse(Object response);
    }
}
