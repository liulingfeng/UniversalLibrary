package com.llf.universallibrary.photo;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.llf.universallibrary.R;
import com.llf.universallibrary.base.BaseActivity;
import com.llf.universallibrary.tools.SettingUtil;
import com.llf.universallibrary.widget.ViewPagerFixed;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by llf on 2016/10/19.
 * 查看图片列表的大图,含有导航点
 */

public class BigImagePagerActivity extends BaseActivity{
    public static void startImagePagerActivity(Activity activity, ArrayList<String> imgUrls, int position){
        Intent intent = new Intent(activity, BigImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, imgUrls);
        intent.putExtra(INTENT_POSITION, position);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in,
                R.anim.slide_out);
    }
    public static final String INTENT_IMGURLS = "imgurls";
    public static final String INTENT_POSITION = "position";
    private ViewPager mViewPager;
    private LinearLayout guideGroup;
    private List<View> guideViewList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_big_image;
    }

    @Override
    public void initView() {
        mViewPager = (ViewPagerFixed)findViewById(R.id.pager);
        guideGroup = (LinearLayout)findViewById(R.id.guideGroup);
        int startPos = getIntent().getIntExtra(INTENT_POSITION, 0);
        ArrayList<String> imgUrls = getIntent().getStringArrayListExtra(INTENT_IMGURLS);
        ImageAdapter adapter = new ImageAdapter(this,imgUrls);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0; i<guideViewList.size(); i++){
                    guideViewList.get(i).setSelected(i==position ? true : false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(startPos);
        addGuideView(guideGroup, startPos, imgUrls);
    }

    private void addGuideView(LinearLayout guideGroup, int startPos, ArrayList<String> imgUrls) {
        if(imgUrls!=null && imgUrls.size()>0){
            guideViewList.clear();
            for (int i=0; i<imgUrls.size(); i++){
                View view = new View(this);
                view.setBackgroundResource(R.drawable.selector_guide_bg);
                view.setSelected(i==startPos ? true : false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(SettingUtil.dip2px(BigImagePagerActivity.this,6),
                        SettingUtil.dip2px(BigImagePagerActivity.this,6));
                layoutParams.setMargins(10, 0, 0, 0);
                guideGroup.addView(view, layoutParams);
                guideViewList.add(view);
            }
        }
    }
    /**
     * 监听返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BigImagePagerActivity.this.finish();
            BigImagePagerActivity.this.overridePendingTransition(R.anim.slide_in,
                    R.anim.slide_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
