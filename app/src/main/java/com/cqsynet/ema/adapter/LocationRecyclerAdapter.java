package com.cqsynet.ema.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cqsynet.ema.R;
import com.cqsynet.ema.model.LocationObject;

import java.util.List;

public class LocationRecyclerAdapter extends BaseQuickAdapter<LocationObject, BaseViewHolder> {

    public LocationRecyclerAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, LocationObject item) {
        helper.setText(R.id.tvName_item_recycler_filter, item.WZ_MS)
                .setChecked(R.id.cb_item_recycler_filter, item.selected)
                .addOnClickListener(R.id.cb_item_recycler_filter);
    }
}
