package com.llf.universallibrary.network.rx;

import com.llf.universallibrary.mvp.BasePresenter;

/**
 * Created by llf on 2016/10/26.
 * 只是一个实例
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
