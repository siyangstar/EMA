package com.cqsynet.ema.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cqsynet.ema.R;
import com.cqsynet.ema.adapter.WorkOrderListAdapter;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.model.WorkOrderObject;
import com.cqsynet.ema.model.WorkOrderResponseObject;
import com.cqsynet.ema.network.OkgoRequest;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkOrderListFragment extends BaseFragment {

    private String mCategory;
    private SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerView mRecyclerView;
    private WorkOrderListAdapter mListAdapter;
    private ArrayList<WorkOrderObject> mItemList;
    private int mNextPage;
    private String mOrderBy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = getArguments().getString("category");
        mItemList = new ArrayList<>();
        mListAdapter = new WorkOrderListAdapter(R.layout.item_recycler_workorder_list, mItemList);

//        for(int i = 0; i < 10; i++) {
//            WorkOrderObject object = new WorkOrderObject();
//            object.BUMEN = "维修部";
//            object.CREATE_DATE = "2018-6-8";
//            object.GD_BM = "13142346";
//            object.GD_ZT = "已完成";
//            object.GD_MS = "描述描述描述描述描述描述描述描述描述描述描述描述描述描述描述描述描述描述描述描述描述描述描述";
//            object.USERNAME = "张三";
//            object.WZ_MS = "大学城";
//            object.ZC_BM = "879432";
//            object.ZC_MS = "休息室空调";
//            mItemList.add(object);
//        }
        if(mCategory.equals(AppConstants.TAG_WORK_ORDER)) {
            mOrderBy = getResources().getStringArray(R.array.workorder_sort_value)[5]; //默认时间排序
            getWorkOrderList(true, 1, mOrderBy);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mSmartRefreshLayout = view.findViewById(R.id.smart_refresh_layout_fragment_list);
        mRecyclerView = view.findViewById(R.id.recyclerview_fragment_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mListAdapter);

        mSmartRefreshLayout.setEnableAutoLoadMore(true);
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getWorkOrderList(false, mNextPage, mOrderBy);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getWorkOrderList(true, 1, mOrderBy);
            }
        });

        mListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        return view;
    }

    /**
     * 获取工单列表
     * @param isRefresh true:刷新  false:加载更多
     * @param pageNo
     * @param orderBy
     */
    private void getWorkOrderList(final boolean isRefresh, final int pageNo, String orderBy) {
        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("start", "");
//        paramMap.put("end", "");
//        paramMap.put("gd_zt", "");
        paramMap.put("bmsj", "1");
        paramMap.put("pageNo", pageNo + "");
        paramMap.put("pageSize", "20");
        paramMap.put("orderBy", orderBy);
        OkgoRequest.excute(mContext, AppConstants.URL_GET_WORK_ORDER, paramMap, new OkgoRequest.IResponseCallback() {
            @Override
            public void onResponse(String response) {
                if (isRefresh) {
                    mSmartRefreshLayout.finishRefresh(true);
                } else {
                    mSmartRefreshLayout.finishLoadMore(true);
                }
                if (response != null) {
                    Gson gson = new Gson();
                    WorkOrderResponseObject responseObj = gson.fromJson(response, WorkOrderResponseObject.class);
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
     * 设置排序方式
     * @param orderBy
     */
    public void setOrderByParam(String orderBy) {
        mOrderBy = orderBy;
        getWorkOrderList(true, 1, mOrderBy);
    }
}
