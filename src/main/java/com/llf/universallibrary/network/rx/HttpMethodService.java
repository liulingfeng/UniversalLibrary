package com.llf.universallibrary.network.rx;

import com.llf.universallibrary.network.HttpResultEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by llf on 2016/10/24.
 * 所有的调用接口,示列
 */

public interface HttpMethodService {
    String BASE_URL = "https://api.douban.com/v2/movie/";

    @GET("top250")
    Observable<HttpResultEntity<MovieEntity>> getTopMovie(@Query("start") int start, @Query("count") int count);
}
