package com.cqsynet.ema.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cqsynet.ema.R;
import com.cqsynet.ema.model.DeviceObject;

import java.util.List;

public class DeviceListAdapter extends BaseQuickAdapter<DeviceObject, BaseViewHolder> {

    public DeviceListAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceObject item) {
        helper.setText(R.id.tvDeviceId_item_recycler_device_list, item.ZC_BM);
        helper.setText(R.id.tvUser_item_recycler_device_list, item.SY_BM_DES);
        helper.setText(R.id.tvLocation_item_recycler_device_list, item.WZ_BM_DES);
        helper.setText(R.id.tvDescription_item_recycler_device_list, item.ZC_MS);
    }
}
