package com.llf.universallibrary.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.llf.universallibrary.R;

/**
 * Created by llf on 2016/10/26.
 * 数据为空提示UI
 */

public class EmptyLayout extends FrameLayout{
    private View mEmptyView;
    private View mBindView;
    private View mErrorView;
    private ImageView mBtnReset;
    private View mLoadingView;
    private TextView tvLoadingText;
    private TextView mErrorText;

    public EmptyLayout(Context context) {
        this(context,null);
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EmptyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        //居中
        params.gravity = Gravity.CENTER;
        //数据为空时的布局
        mEmptyView = View.inflate(context, R.layout.layout_empty, this);
        addView(mEmptyView,params);
        //加载中的布局
        mLoadingView = View.inflate(context, R.layout.layout_loading, this);
        tvLoadingText =(TextView)mLoadingView.findViewById(R.id.tv_loading);
        addView(mLoadingView,params);
        //错误时的布局
        mErrorView = View.inflate(context, R.layout.layout_error, this);
        mBtnReset =(ImageView) mErrorView.findViewById(R.id.iv_error);
        mErrorText = (TextView) mErrorView.findViewById(R.id.tv_error);
        addView(mErrorView, params);
        //全部隐藏
        setGone();
    }

    /**
     * 全部隐藏
     */
    private void setGone() {
        mEmptyView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
    }

    /**
     * 加载中
     */
    public void showLoading(String text) {
        if (mBindView != null) mBindView.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(text)) tvLoadingText.setText(text);
        setGone();
        mLoadingView.setVisibility(View.VISIBLE);
    }
    public void showLoading() {
        showLoading(null);
    }
    /**
     * 加载失败
     */
    public void showError() {
        showError(null);
    }
    public void showError(String text) {
        if (mBindView != null) mBindView.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(text)) mErrorText.setText(text);
        setGone();
        mErrorView.setVisibility(View.VISIBLE);
    }
    /**
     * 数据为空
     */
    public void showEmpty() {
        if (mBindView != null) mBindView.setVisibility(View.GONE);
        setGone();
        mEmptyView.setVisibility(View.VISIBLE);
    }
    /**
     * 加载成功
     */
    public void showSuccess() {
        if (mBindView != null) mBindView.setVisibility(View.VISIBLE);
        setGone();
    }

    /**
     * 首先调用
     * @param view
     */
    public void bindView(View view) {
        mBindView = view;
    }

    public void setOnButtonClick(OnClickListener listener) {
        mBtnReset.setOnClickListener(listener);
    }
}
