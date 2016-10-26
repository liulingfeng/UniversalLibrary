package com.llf.universallibrary.photopicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.llf.universallibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxs on 2016/10/10.
 */

public class GalleryAdapter extends BaseAdapter {
    private LayoutInflater infalter;
    private List<Image> datas;
    private Context context;
    private boolean showCamera;
    private boolean mutiSelect;
    private List<Image> selectedImageList = new ArrayList<>();

    public GalleryAdapter(Context context,List<Image> datas, ImgSelConfig config) {
        infalter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.datas = datas;
        this.showCamera = config.needCamera;
        this.mutiSelect = config.multiSelect;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Image getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = infalter.inflate(R.layout.item_img_sel, parent, false);
            holder = new ViewHolder();
            holder.iv_photo = (ImageView)convertView.findViewById(R.id.ivImage);
            holder.iv_select = (ImageView)convertView.findViewById(R.id.ivPhotoCheaked);
            holder.fl_shade = (FrameLayout)convertView.findViewById(R.id.pi_picture_choose_item_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0&& showCamera) {
            holder.iv_photo.setImageResource(R.drawable.ic_take_photo);
            holder.iv_select.setVisibility(View.GONE);
            return convertView;
        }else{
            holder.iv_photo.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        }
        Glide.with(context).load(datas.get(position).path).into(holder.iv_photo);

        if (mutiSelect) {
            holder.iv_select.setVisibility(View.VISIBLE);
            if (selectedImageList.contains(datas.get(position))) {
                holder.iv_select.setImageResource(R.drawable.ic_checked);
                holder.fl_shade.setVisibility(View.VISIBLE);
            } else {
                holder.iv_select.setImageResource(R.drawable.ic_uncheck);
                holder.fl_shade.setVisibility(View.GONE);
            }
        } else {
            holder.iv_select.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView iv_photo;
        ImageView iv_select;
        FrameLayout fl_shade;
    }

    public void select(Image image) {
        if (selectedImageList.contains(image)) {
            selectedImageList.remove(image);
        } else {
            selectedImageList.add(image);
        }
        notifyDataSetChanged();
    }
}
