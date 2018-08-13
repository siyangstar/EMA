package com.cqsynet.ema.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cqsynet.ema.R;
import com.cqsynet.ema.db.DictionaryDao;
import com.cqsynet.ema.model.AssetObject;
import com.cqsynet.ema.model.DictionaryObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 资产列表的适配器
 */
public class AssetListAdapter extends BaseQuickAdapter<AssetObject, BaseViewHolder> {

    public AssetListAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AssetObject item) {
        helper.setText(R.id.tvName_item_recycler_asset_list, item.zcMs);
        helper.setText(R.id.tvNumber_item_recycler_asset_list, item.zcBm);
        helper.setText(R.id.tvUser_item_recycler_asset_list, item.glZrr);
        helper.setText(R.id.tvLocation_item_recycler_asset_list, item.wzBmDes);
        helper.setText(R.id.tvManageDepartment_item_recycler_asset_list, item.glBmDes);
        helper.setText(R.id.tvUseDepartment_item_recycler_asset_list, item.syBmDes);
        helper.setText(R.id.tvRepairDepartment_item_recycler_asset_list, item.wxBmDes);
        helper.getView(R.id.tvStatus_item_recycler_asset_list).setVisibility(View.INVISIBLE);
        ArrayList<DictionaryObject> list = DictionaryDao.getInstance(mContext).queryDictionaryList("zczt");
        Iterator<DictionaryObject> iterator = list.iterator();
        while (iterator.hasNext()) {
            DictionaryObject object = iterator.next();
            if (object.value.equals(item.zcZt)) {
                helper.setText(R.id.tvStatus_item_recycler_asset_list, object.label);
                helper.getView(R.id.tvStatus_item_recycler_asset_list).setVisibility(View.VISIBLE);
            }
        }
    }
}
