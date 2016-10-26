package com.llf.universallibrary.network.rx;

import com.llf.universallibrary.mvp.BaseView;

/**
 * Created by llf on 2016/10/26.
 * 只是一个实例
 */

public interface TestView extends BaseView{
    void getDataSuccess(MovieEntity model);
    void getDataFail(String msg);
}
