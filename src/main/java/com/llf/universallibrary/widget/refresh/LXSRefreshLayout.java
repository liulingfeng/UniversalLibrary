package com.llf.universallibrary.widget.refresh;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import com.llf.universallibrary.tools.SettingUtil;

/**
 * Created by 刘灵锋 on 2016/10/27.
 * 自己搞一个
 */

public class LXSRefreshLayout extends FrameLayout{
    //头部layout
    protected FrameLayout mHeadLayout;
    //真正的头部布局
    private IHeaderView mHeadView;
    //底部layout
    private FrameLayout mBottomLayout;
    //真正的底部布局
    private IBottomView mBottomView;
    //主控件
    private View mChildView;

    private float mTouchY;
    private int state;
    private static final int PULL_DOWN_REFRESH = 1;
    private static final int PULL_UP_LOAD = 2;

    private int bottomHeight,refreshHeadHeight;
    //刷新的状态
    protected boolean isRefreshing;
    //加载更多的状态
    protected boolean isLoadingmore;
    private OnRefreshListener mOnRefreshListener;

    public LXSRefreshLayout(Context context) {
        this(context,null);
    }

    public LXSRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LXSRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        bottomHeight = SettingUtil.dip2px(context,48);
        refreshHeadHeight = SettingUtil.dip2px(context,72);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //添加头部
        if (mHeadLayout == null) {
            FrameLayout headViewLayout = new FrameLayout(getContext());
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            layoutParams.gravity = Gravity.TOP;
            headViewLayout.setLayoutParams(layoutParams);
            headViewLayout.setBackgroundColor(Color.parseColor("#F2F2F2"));
            mHeadLayout = headViewLayout;
            this.addView(mHeadLayout);
            if (mHeadView == null) setHeaderView(new SinaRefreshView(getContext()));
        }

        //添加底部
        if (mBottomLayout == null) {
            FrameLayout bottomViewLayout = new FrameLayout(getContext());
            LayoutParams layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            layoutParams2.gravity = Gravity.BOTTOM;
            bottomViewLayout.setLayoutParams(layoutParams2);
            bottomViewLayout.setBackgroundColor(Color.parseColor("#F2F2F2"));
            mBottomLayout = bottomViewLayout;
            this.addView(mBottomLayout);

            if (mBottomView == null) {
                LoadingView loadingView = new LoadingView(getContext());
                setBottomView(loadingView);
            }
        }

        mChildView = getChildAt(0);
        if (mChildView == null) return;
    }

    /**
     * 设置头部View
     */
    public void setHeaderView(final IHeaderView headerView) {
        if (headerView != null) {
            mHeadLayout.removeAllViewsInLayout();
            mHeadLayout.addView(headerView.getView());
            mHeadView = headerView;
        }
    }

    /**
     * 设置底部View
     */
    public void setBottomView(final IBottomView bottomView) {
        if (bottomView != null) {
            mBottomLayout.removeAllViewsInLayout();
            mBottomLayout.addView(bottomView.getView());
            mBottomView = bottomView;
        }
    }

    /**
     * 拦截触摸事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = event.getY() - mTouchY;
                if (dy > 0 && !ScrollingUtil.canChildScrollUp(mChildView)){
                    state = PULL_DOWN_REFRESH;
                    return true;
                }else if (dy < 0 && !ScrollingUtil.canChildScrollDown(mChildView)){
                    state = PULL_UP_LOAD;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onInterceptTouchEvent(event);
    }
    /**
     * 触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isRefreshing || isLoadingmore) return super.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float dy = event.getY() - mTouchY;
                float offsetY = dy/2;
                if (state == PULL_DOWN_REFRESH) {
                        dy = Math.max(0, offsetY);
                        mChildView.setTranslationY(dy);
                        mHeadLayout.getLayoutParams().height = (int) dy;
                        mHeadLayout.requestLayout();
                        if(dy/refreshHeadHeight<1.02)
                        mHeadView.onPullingDown(offsetY/refreshHeadHeight,refreshHeadHeight,refreshHeadHeight);
                }else if (state == PULL_UP_LOAD) {
                    if(offsetY>0){
                        break;
                    }
                        dy = Math.min(bottomHeight, Math.abs(offsetY));
                        dy = Math.max(0, dy);
                        mChildView.setTranslationY(-dy);
                        mBottomLayout.getLayoutParams().height = (int)dy;
                        mBottomLayout.requestLayout();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (state == PULL_DOWN_REFRESH) {
                    if(mChildView.getTranslationY() >= refreshHeadHeight){
                        animChildView(refreshHeadHeight);
                        isRefreshing = true;
                        mHeadView.startAnim(refreshHeadHeight,refreshHeadHeight);
                        if(mOnRefreshListener!=null)
                            mOnRefreshListener.onRefresh(LXSRefreshLayout.this);
                    }else{
                        animChildView(0);
                    }
                } else if (state == PULL_UP_LOAD) {
                    if(Math.abs(mChildView.getTranslationY()) >= bottomHeight){
                        isLoadingmore = true;
                        mBottomView.startAnim(bottomHeight,bottomHeight);
                        animChildView(-bottomHeight);
                        if(mOnRefreshListener!=null)
                            mOnRefreshListener.onLoadMore(LXSRefreshLayout.this);
                    }else{
                        animChildView(0);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 执行动画
     * @param endValue
     */
    private void animChildView(float endValue) {
        animChildView(endValue, 300);
    }

    private void animChildView(float endValue, long duration) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(mChildView, "translationY", mChildView.getTranslationY(), endValue);
        oa.setDuration(duration);
        oa.setInterpolator(new DecelerateInterpolator());//设置速率为递减
        oa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) mChildView.getTranslationY();
                if (state == PULL_DOWN_REFRESH) {
                    mHeadLayout.getLayoutParams().height = height;
                    mHeadLayout.requestLayout();
                    mHeadView.onPullReleasing(height/refreshHeadHeight,refreshHeadHeight,refreshHeadHeight);
                }else if (state == PULL_UP_LOAD) {
                    mBottomLayout.getLayoutParams().height = -height;
                    mBottomLayout.requestLayout();
                }
            }
        });
        oa.start();
    }

    /**
     * 刷新结束
     */
    public void finishRefreshing() {
        isRefreshing = false;
        if (mChildView != null) {
            animChildView(0f);
        }
    }

    /**
     * 加载更多结束
     */
    public void finishLoadmore() {
        isLoadingmore = false;
        if (mChildView != null) {
            animChildView(0f);
            mBottomView.onFinish();
        }
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    public interface OnRefreshListener{
        void onRefresh(LXSRefreshLayout refreshLayout);
        void onLoadMore(LXSRefreshLayout refreshLayout);
    }
}
