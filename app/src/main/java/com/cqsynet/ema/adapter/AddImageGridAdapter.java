package com.cqsynet.ema.adapter;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cqsynet.ema.R;
import com.cqsynet.swifi.GlideApp;

import java.io.File;
import java.util.ArrayList;

/**
 * 提交报修时选择图片的适配器
 */
public class AddImageGridAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public AddImageGridAdapter(int layoutResId, ArrayList<String> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String imagePath) {
        if(imagePath.equals("")) {
            helper.setImageResource(R.id.ivImage_item_add_image_grid, R.drawable.addimg);
        } else {
            GlideApp.with(mContext)
                    .load(new File(imagePath))
//                    .downsample(DownsampleStrategy.AT_LEAST)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into((ImageView) helper.getView(R.id.ivImage_item_add_image_grid));
        }
    }
}
