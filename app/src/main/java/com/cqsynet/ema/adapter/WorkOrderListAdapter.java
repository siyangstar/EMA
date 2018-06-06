package com.cqsynet.ema.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cqsynet.ema.R;
import com.cqsynet.ema.model.WorkOrderObject;

import java.util.List;

public class WorkOrderListAdapter extends BaseQuickAdapter<WorkOrderObject, BaseViewHolder> {

    public WorkOrderListAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkOrderObject item) {
        helper.setText(R.id.tvOrderId_item_recycler_workorder_list, item.GD_BM);
        helper.setText(R.id.tvStatus_item_recycler_workorder_list, item.GD_ZT);
        helper.setText(R.id.tvOrderDesc_item_recycler_workorder_list, item.GD_MS);
        helper.setText(R.id.tvDeviceId_item_recycler_workorder_list, item.ZC_BM);
        helper.setText(R.id.tvDeviceDesc_item_recycler_workorder_list, item.ZC_MS);
        helper.setText(R.id.tvLocation_item_recycler_workorder_list, item.WZ_MS);
        helper.setText(R.id.tvDate_item_recycler_workorder_list, item.CREATE_DATE);
        helper.setText(R.id.tvPerson_item_recycler_workorder_list, item.USERNAME);
        helper.setText(R.id.tvDepartment_item_recycler_workorder_list, item.BUMEN);
    }
}
