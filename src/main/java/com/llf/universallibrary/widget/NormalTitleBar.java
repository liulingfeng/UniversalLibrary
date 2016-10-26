package com.llf.universallibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.llf.universallibrary.R;

/**
 * 通用的头部
 */
public class NormalTitleBar extends RelativeLayout {
    private TextView ivBack,tvTitle, tvRight;

    public NormalTitleBar(Context context) {
        super(context, null);
    }

    public NormalTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.layout_head, this);
        ivBack = (TextView) findViewById(R.id.tv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
    }

    /**
     * 管理返回按钮
     */
    public void setBackVisibility(boolean visible) {
        ivBack.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题栏左侧字符串
     * @param tvLeftText
     */
    public void setTvLeft(String tvLeftText){
        ivBack.setText(tvLeftText);
    }

    public void setTitleText(String string) {
        tvTitle.setText(string);
    }

    public void setTitleText(int string) {
        tvTitle.setText(string);
    }

    public void setTitleColor(int color) {
        tvTitle.setTextColor(color);
    }

    /**
     * 右标题
     */
    public void setRightTitleVisibility(boolean visible) {
        tvRight.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setRightTitle(String text) {
        tvRight.setText(text);
    }

    /*
     * 点击事件
     */
    public void setOnBackListener(OnClickListener listener) {
        ivBack.setOnClickListener(listener);
    }

    public void setOnRightTextListener(OnClickListener listener) {
        tvRight.setOnClickListener(listener);
    }
}
