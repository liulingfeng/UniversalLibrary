package com.llf.universallibrary.common;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用的ViewHolder
 * @author llf
 *
 */
public class CommonViewHolder {
	private final SparseArray<View> mViews;
	private View mConvertView;

	private CommonViewHolder(Context context, ViewGroup parent, int layoutId) {
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @return
	 */
	public static CommonViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId) {

		if (convertView == null) {
			return new CommonViewHolder(context, parent, layoutId);
		}
		return (CommonViewHolder) convertView.getTag();
	}

	/**
	 *
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public CommonViewHolder setText(int viewId, CharSequence text) {
		TextView view = (TextView) getView(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public CommonViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = (ImageView) getView(viewId);
		view.setImageResource(drawableId);

		return this;
	}

	/**
	 * 
	 * 
	 * @param viewId
	 * @return
	 */
	public View getView(int viewId) {

		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return view;
	}

	public View getConvertView() {
		return mConvertView;
	}
}
