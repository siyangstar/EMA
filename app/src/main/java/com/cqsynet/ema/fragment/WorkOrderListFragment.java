package com.cqsynet.ema.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cqsynet.ema.R;
import com.cqsynet.ema.adapter.WorkOrderListAdapter;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.model.WorkOrderResponseObject;
import com.cqsynet.ema.network.OkgoRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 列表模块(工单列表,报修列表共用)
 */
public class WorkOrderListFragment extends BaseFragment {

    private String mCategory;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private WorkOrderListAdapter mListAdapter;
    private int mNextPage;
    private String mOrderBy;
    private String mStartDate = "";
    private String mEndDate = "";
    private String mStatus = "";
    private String mOnlyMine = "1";
    private EditText mEtSearch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = getArguments().getString("category");
        mListAdapter = new WorkOrderListAdapter(R.layout.item_recycler_workorder_list, new ArrayList<>());

        if(mCategory.equals(AppConstants.TAG_WORK_ORDER)) {
            mOrderBy = ""; //默认排序
            getWorkOrderList(mStartDate, mEndDate, mStatus, mOnlyMine,true, 1, mOrderBy, "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.smart_refresh_layout_fragment_list);
        mRecyclerView = view.findViewById(R.id.recyclerview_fragment_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mListAdapter);
        View headerView = View.inflate(mContext, R.layout.view_search, null);
        mEtSearch = headerView.findViewById(R.id.etSearch_view_search);
        mListAdapter.addHeaderView(headerView);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //执行搜索
                    getWorkOrderList(mStartDate, mEndDate, mStatus, mOnlyMine,true, 1, mOrderBy, mEtSearch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWorkOrderList(mStartDate, mEndDate, mStatus, mOnlyMine,true, 1, mOrderBy, mEtSearch.getText().toString().trim());
            }
        });

        mListAdapter.setEnableLoadMore(true);
        mListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        mListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getWorkOrderList(mStartDate, mEndDate, mStatus, mOnlyMine,false, mNextPage, mOrderBy, mEtSearch.getText().toString().trim());
            }
        }, mRecyclerView);

        return view;
    }


    /**
     *
     * 获取工单列表
     * @param isRefresh true:刷新  false:加载更多
     * @param startDate
     * @param endDate
     * @param status
     * @param onlyMine 0:只看自己 1:看整个部门
     * @param pageNo
     * @param orderBy
     */
    private void getWorkOrderList(String startDate, String endDate, String status, String onlyMine, final boolean isRefresh, final int pageNo, String orderBy, String searchValue) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("start", startDate);
        paramMap.put("end", endDate);
        paramMap.put("gd_zt", status);
        paramMap.put("bmsj", onlyMine);
        paramMap.put("pageNo", pageNo + "");
        paramMap.put("pageSize", "20");
        paramMap.put("orderBy", orderBy);
        paramMap.put("searchVal", searchValue);
        OkgoRequest.excute(mContext, AppConstants.URL_GET_WORK_ORDER, paramMap, new OkgoRequest.IResponseCallback() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (response != null) {
                    Gson gson = new Gson();
                    WorkOrderResponseObject responseObj = gson.fromJson(response, WorkOrderResponseObject.class);
                    if (responseObj != null) {
                        if (AppConstants.RET_OK.equals(responseObj.ret)) {
                            if(isRefresh) {
                                mListAdapter.setNewData(responseObj.data.data.list);
                                mRecyclerView.scrollToPosition(0);
                            } else {
                                mListAdapter.addData(responseObj.data.data.list);
                                mListAdapter.loadMoreComplete();
                            }
                            if(responseObj.data.data.count > mListAdapter.getData().size()) {
                                mNextPage = responseObj.data.data.pageNo + 1;
                            } else {
                                mListAdapter.loadMoreEnd();
                            }
                        } else {
                            mListAdapter.loadMoreFail();
                            ToastUtils.showShort(responseObj.msg);
                        }
                    } else {
                        mListAdapter.loadMoreFail();
                        ToastUtils.showShort(R.string.request_failed);
                    }
                }
            }

            @Override
            public void onErrorResponse() {
                mListAdapter.loadMoreFail();
                mSwipeRefreshLayout.setRefreshing(false);
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
        getWorkOrderList(mStartDate, mEndDate, mStatus, mOnlyMine,true, 1, mOrderBy, mEtSearch.getText().toString().trim());
    }

    /**
     * 设置过滤条件
     * @param startDate
     * @param endDate
     * @param status
     * @param onlyMine
     */
    public void setFilter(String startDate, String endDate, String status, String onlyMine) {
        mStartDate = startDate;
        mEndDate = endDate;
        mStatus = status;
        mOnlyMine = onlyMine;
        getWorkOrderList(mStartDate, mEndDate, mStatus, mOnlyMine,true, 1, mOrderBy, mEtSearch.getText().toString().trim());
    }
}
