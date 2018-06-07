package com.cqsynet.ema.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cqsynet.ema.R;
import com.cqsynet.ema.model.HomeGridObject;

import java.util.List;

/**
 * 首页表格的适配器
 */
public class HomeGridAdapter extends BaseQuickAdapter<HomeGridObject, BaseViewHolder> {

    public HomeGridAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, HomeGridObject item) {
        helper.setImageResource(R.id.ivIcon_item_home_grid, item.imageRes);
        helper.setText(R.id.tvTitle_item_home_grid, item.title);
    }
}
