package com.cqsynet.ema.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cqsynet.ema.R;
import com.cqsynet.ema.db.DictionaryDao;
import com.cqsynet.ema.model.TodoObject;

import java.util.List;

/**
 * 待办任务列表的适配器
 */
public class TodoListAdapter extends BaseQuickAdapter<TodoObject, BaseViewHolder> {

    public TodoListAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TodoObject item) {
        if(item != null && item.data != null) {
            helper.setText(R.id.tvOrderId_item_recycler_workorder_list, item.data.gdBm);
            if (!TextUtils.isEmpty(item.data.lcZt)) {
                helper.setText(R.id.tvStatus_item_recycler_workorder_list, DictionaryDao.getInstance(mContext).queryDictionary("process", item.data.lcZt));
                helper.getView(R.id.tvStatus_item_recycler_workorder_list).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.tvStatus_item_recycler_workorder_list).setVisibility(View.GONE);
            }
            helper.setText(R.id.tvOrderDesc_item_recycler_workorder_list, item.data.gdMs);
            helper.setText(R.id.tvDeviceId_item_recycler_workorder_list, item.data.sbBm);
            helper.setText(R.id.tvDeviceDesc_item_recycler_workorder_list, item.data.sbBmDes);
            helper.setText(R.id.tvLocation_item_recycler_workorder_list, item.data.wzBmDes);
            helper.setText(R.id.tvDate_item_recycler_workorder_list, item.data.bxDate);
            helper.setText(R.id.tvPerson_item_recycler_workorder_list, item.data.bxr);
            helper.setText(R.id.tvDepartment_item_recycler_workorder_list, item.data.bmBmDes);
        }
    }
}
