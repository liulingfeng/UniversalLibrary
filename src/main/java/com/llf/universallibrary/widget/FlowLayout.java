package com.llf.universallibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by llf on 2016/10/17.
 * 流式布局
 */

public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
/*  只有在内容为wrap情况下才需要记录width和height  width：总宽度  height：总高度     lineWidth:行宽  lineHeight ：行高 */
        int width = 0;
        int height = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            lineHeight = Math.max(lineHeight, childHeight);
            lineWidth += childWidth;
            if (lineWidth > sizeWidth) {
                lineWidth = childWidth;
                height += lineHeight;
            } else {
                width = Math.max(width, lineWidth);
            }
            if (i == getChildCount() - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        int width = getWidth();//ViewGroup的总宽
        int height = 0;//目前的行高
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int chidWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            left = lp.leftMargin + lineWidth;
            right = lp.rightMargin + chidWidth + lp.leftMargin + lineWidth;
            top = lp.topMargin + height;
            bottom = lp.bottomMargin + childHeight + lp.topMargin + height;
            lineWidth = right;
            if (lineWidth > width) {
                lineWidth = 0;
                left = lp.leftMargin + lineWidth;
                right = lp.rightMargin + chidWidth + lp.leftMargin + lineWidth;
                height += lineHeight;
                top = lp.topMargin + height;
                bottom = lp.bottomMargin + childHeight + lp.topMargin + height;
                lineWidth = right;
            }
            child.layout(left, top, right - lp.rightMargin, bottom - lp.bottomMargin);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
