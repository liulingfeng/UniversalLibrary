package com.llf.universallibrary.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by llf on 2016/11/1.
 * 通用的fragmentpageAdapter
 */

public class FragmentAdapter extends FragmentPagerAdapter{
    public FragmentManager fm;
    private List<Fragment> list;
    private String[] titles;

    public FragmentAdapter(FragmentManager fm,List<Fragment> list,String[] titles) {
        super(fm);
        this.fm = fm;
        this.list = list;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
    /**
     * 返回每个Tab的标题
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    /**
     * 这边并没有创建销毁过程，只创建一次
     * @param container
     * @param position
     * @return
     */
    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fm.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = list.get(position);
        fm.beginTransaction().hide(fragment).commit();
    }
}
