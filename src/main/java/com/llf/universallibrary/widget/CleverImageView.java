package com.llf.universallibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by llf on 2016/10/17.
 * 自动适配的ImageView
 */

public class CleverImageView extends ImageView {
    private double scale = 3.6;//宽高比

    public CleverImageView(Context context) {
        this(context, null);
    }

    public CleverImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CleverImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        if (modeWidth == MeasureSpec.EXACTLY && modeHeight == MeasureSpec.AT_MOST) {
            sizeHeight = (int) (sizeWidth / scale + 0.5f);//这里加0.5f是为了四舍五入
        } else if (modeHeight == MeasureSpec.EXACTLY && modeWidth == MeasureSpec.AT_MOST) {
            sizeWidth = (int) (sizeHeight / scale + 0.5f);
        }
        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
}
