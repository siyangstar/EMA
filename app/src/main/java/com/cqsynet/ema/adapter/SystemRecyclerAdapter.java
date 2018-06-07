package com.cqsynet.ema.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cqsynet.ema.R;
import com.cqsynet.ema.model.SystemCategoryObject;

import java.util.List;

/**
 * 筛选界面中系统分类列表的适配器
 */
public class SystemRecyclerAdapter extends BaseQuickAdapter<SystemCategoryObject, BaseViewHolder> {

    public SystemRecyclerAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, SystemCategoryObject item) {
        helper.setText(R.id.tvName_item_recycler_filter, item.ZL_MS)
                .setChecked(R.id.cb_item_recycler_filter, item.selected)
                .addOnClickListener(R.id.cb_item_recycler_filter);
    }
}
