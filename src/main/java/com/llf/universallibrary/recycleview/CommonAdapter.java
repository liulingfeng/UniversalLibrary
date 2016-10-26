package com.llf.universallibrary.recycleview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的ListView适配器
 * @author llf
 *
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter{
	private Context context;
	private List<T> datas;
	private int layoutId;
	
	public CommonAdapter(Context context,List<T> datas,int layoutId){
		this.context = context;
		this.datas = datas;
		this.layoutId = layoutId;
	}
	/**
	 * 分页调用
	 * */
	public CommonAdapter(Context context, int layoutId){
		this.context = context;
		this.datas = new ArrayList<T>();
		this.layoutId = layoutId;
	}
	/**
	 * 分页调用
	 * */
	public void addData(List<T> datas){
		if(datas==null|| datas.size()<1)
			return;
			this.datas.addAll(datas);
		notifyDataSetChanged();
	}
	/**
	 * 分页调用
	 * */
	public void setData(List<T> datas){
		if(datas!=null ){
			this.datas=datas;
			notifyDataSetChanged();
		}
	}
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public T getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommonViewHolder viewHolder = CommonViewHolder.get(context, convertView, parent, layoutId);
		convert(viewHolder, getItem(position),position);
		return viewHolder.getConvertView();
	}
	public abstract void convert(CommonViewHolder viewHolder,T item,int position);
	
}
