package com.llf.universallibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 按条数排满的ListView
 */

public class NestListView extends ListView{
	public NestListView(Context context) {
		super(context);
	}
	/**
	 * @param context
	 * @param attrs
	 */
	public NestListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public NestListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	 @Override
     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
             int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                             MeasureSpec.AT_MOST);
             super.onMeasure(widthMeasureSpec, expandSpec);
     }
	 
}

