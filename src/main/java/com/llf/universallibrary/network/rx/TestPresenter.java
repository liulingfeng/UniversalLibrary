package com.llf.universallibrary.network.rx;

import com.llf.universallibrary.mvp.BasePresenter;

/**
 * Created by llf on 2016/10/26.
 * 只是一个实例
 * http://gank.io/post/560e15be2dca930e00da1083#toc_1 rxjava
 */

public class TestPresenter extends BasePresenter<TestView> {
    public TestPresenter(TestView view) {
        attachView(view);
    }

    public void loadDataByRetrofitRxjava(int start, int count) {
        mvpView.showLoading();
        addSubscription(apiStores.getTopMovie(start,count),
                new ApiCallback<MovieEntity>() {
                    @Override
                    public void onSuccess(MovieEntity model) {
                        mvpView.getDataSuccess(model);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.getDataFail(msg);
                    }


                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }

                });
    }
}
