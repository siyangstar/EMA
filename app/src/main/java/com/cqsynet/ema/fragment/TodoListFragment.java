package com.cqsynet.ema.fragment;

import android.content.Intent;
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
import com.cqsynet.ema.activity.WebActivity;
import com.cqsynet.ema.adapter.TodoListAdapter;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.model.TodoListResponseObject;
import com.cqsynet.ema.model.WorkOrderObject;
import com.cqsynet.ema.network.OkgoRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 待办任务列表
 */
public class TodoListFragment extends BaseFragment {

    private String mCategory;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TodoListAdapter mListAdapter;
    private int mNextPage;
    private EditText mEtSearch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = getArguments().getString("category");
        mListAdapter = new TodoListAdapter(R.layout.item_recycler_workorder_list, new ArrayList<>());

        getTodoList(true, 1, "");
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
                    getTodoList(true, 1, mEtSearch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTodoList(true, 1, mEtSearch.getText().toString().trim());
            }
        });

        mListAdapter.setEnableLoadMore(true);
        mListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WorkOrderObject object = mListAdapter.getData().get(position).data;
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("url", AppConstants.URL_WORKORDER_DETAIL);
                intent.putExtra("category", mCategory);
                intent.putExtra("id", object.id);
                intent.putExtra("title", getString(R.string.workorder_detail));
                startActivity(intent);
            }
        });

        mListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getTodoList(false, mNextPage, mEtSearch.getText().toString().trim());
            }
        }, mRecyclerView);

        return view;
    }


    /**
     * 获取待办任务列表
     * @param isRefresh
     * @param pageNo
     * @param searchValue
     */
    private void getTodoList(final boolean isRefresh, final int pageNo, String searchValue) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("procDefKey", "act_gong_dan_yan_shi");
        paramMap.put("pageNo", pageNo + "");
        paramMap.put("pageSize", "20");
        paramMap.put("searchVal", searchValue);
        String url;
        if(mCategory.equals(getString(R.string.todolist_no))) {
            url = AppConstants.URL_TODOLIST_NO;
        } else {
            url = AppConstants.URL_TODOLIST_YES;
        }
        OkgoRequest.excute(mContext, url, paramMap, new OkgoRequest.IResponseCallback() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (response != null) {
                    Gson gson = new Gson();
                    TodoListResponseObject responseObj = gson.fromJson(response, TodoListResponseObject.class);
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
}
