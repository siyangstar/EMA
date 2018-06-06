package com.cqsynet.ema.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cqsynet.ema.R;
import com.cqsynet.ema.adapter.DeviceListAdapter;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.fragment.FilterFragment;
import com.cqsynet.ema.model.DeviceListResponseObject;
import com.cqsynet.ema.model.DeviceObject;
import com.cqsynet.ema.model.MessageEvent;
import com.cqsynet.ema.network.OkgoRequest;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DeviceListActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout mFlFilter;
    private FilterFragment mFilterFragment;
    private SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerView mRecyclerView;
    private DeviceListAdapter mListAdapter;
    private ArrayList<DeviceObject> mItemList;
    private int mNextPage;
    private String mOrderBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        mSmartRefreshLayout = findViewById(R.id.smart_refresh_layout_activity_device_list);
        mRecyclerView = findViewById(R.id.recyclerview_activity_device_list);
        mFlFilter = findViewById(R.id.flFilter_activity_device_list);

        findViewById(R.id.tvSort_sort_filter).setOnClickListener(this);
        findViewById(R.id.tvFilter_sort_filter).setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mItemList = new ArrayList<>();
        mListAdapter = new DeviceListAdapter(R.layout.item_recycler_device_list, mItemList);
        mRecyclerView.setAdapter(mListAdapter);

        mSmartRefreshLayout.setEnableAutoLoadMore(true);
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getDeviceList(false, mNextPage, mOrderBy);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getDeviceList(true, 1, mOrderBy);
            }
        });

        mListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        mFilterFragment = new FilterFragment();
        getFragmentManager().beginTransaction().add(R.id.flFilter_activity_device_list, mFilterFragment).commitAllowingStateLoss();

        mOrderBy = getResources().getStringArray(R.array.device_sort_value)[0];
        getDeviceList(true, 1, mOrderBy);
    }

    private void getDeviceList(final boolean isRefresh, final int pageNo, String orderBy) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("wz", "");
        paramMap.put("zl", "");
        paramMap.put("key", "");
        paramMap.put("pageNo", pageNo + "");
        paramMap.put("pageSize", "20");
        paramMap.put("orderBy", orderBy);
        OkgoRequest.excute(this, AppConstants.URL_GET_DEVICE, paramMap, new OkgoRequest.IResponseCallback() {
            @Override
            public void onResponse(String response) {
                if (isRefresh) {
                    mSmartRefreshLayout.finishRefresh(true);
                } else {
                    mSmartRefreshLayout.finishLoadMore(true);
                }
                if (response != null) {
                    Gson gson = new Gson();
                    DeviceListResponseObject responseObj = gson.fromJson(response, DeviceListResponseObject.class);
                    if (responseObj != null) {
                        if (AppConstants.RET_OK.equals(responseObj.ret)) {
                            if(isRefresh) {
                                mItemList.clear();
                            }
                            mItemList.addAll(responseObj.data.data.list);
                            if(responseObj.data.data.count == responseObj.data.data.pageSize) {
                                mNextPage = responseObj.data.data.pageNo + 1;
                            } else {
                                mSmartRefreshLayout.setNoMoreData(true);
                            }
                            mListAdapter.notifyDataSetChanged();
                            if(isRefresh) {
                                mRecyclerView.scrollToPosition(0);
                            }
                        } else {
                            ToastUtils.showShort(responseObj.msg);
                        }
                    } else {
                        ToastUtils.showShort(R.string.request_failed);
                    }
                }
            }

            @Override
            public void onErrorResponse() {
                if (isRefresh) {
                    mSmartRefreshLayout.finishRefresh(true);
                } else {
                    mSmartRefreshLayout.finishLoadMore(true);
                }
                ToastUtils.showShort(R.string.request_failed);
            }
        });
    }

    /**
     * 显示/隐藏 筛选界面
     *
     */
    public <T extends Fragment> void switchFilter() {
        if(mFlFilter.getVisibility() == View.VISIBLE) {
            mFlFilter.setVisibility(View.GONE);
        } else {
            mFlFilter.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSort_sort_filter:
                new MaterialDialog.Builder(this)
                        .titleGravity(GravityEnum.CENTER)
                        .title(R.string.device_sort)
                        .dividerColorRes(R.color.divider)
                        .itemsGravity(GravityEnum.CENTER)
                        .items(R.array.device_sort)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                mOrderBy = getResources().getStringArray(R.array.device_sort_value)[which];
                                getDeviceList(true, 1, mOrderBy);
                            }
                        })
                        .show();
                break;
            case R.id.tvFilter_sort_filter:
                switchFilter();
                break;
        }
    }

    /**
     * 消息监听
     * @param messageEvent
     */
    @Override
    public void onMessageEvent(MessageEvent messageEvent) {
        Bundle bundle = messageEvent.getMessage();
        String type = bundle.getString("type");
        if(type.equals("cancelFilter")) {
            mFlFilter.setVisibility(View.GONE);
        } else if (type.equals("confirmFilter")) {
            mFlFilter.setVisibility(View.GONE);
        }
    }
}
