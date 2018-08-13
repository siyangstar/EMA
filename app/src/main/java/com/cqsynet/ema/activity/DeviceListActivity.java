package com.cqsynet.ema.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cqsynet.ema.R;
import com.cqsynet.ema.adapter.DeviceListAdapter;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.fragment.DeviceFilterFragment;
import com.cqsynet.ema.model.DeviceListResponseObject;
import com.cqsynet.ema.model.DeviceObject;
import com.cqsynet.ema.model.MessageEvent;
import com.cqsynet.ema.network.OkgoRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 设备列表
 */
public class DeviceListActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvSort;
    private FrameLayout mFlFilter;
    private DeviceFilterFragment mDeviceFilterFragment;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private DeviceListAdapter mListAdapter;
    private ArrayList<DeviceObject> mItemList;
    private int mNextPage;
    private String mOrderBy;
    private String mFilterLocation = "";
    private String mFilterSystemCategory = "";
    private EditText mEtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        ((TextView) findViewById(R.id.tvTitle_titlebar)).setText(R.string.select_device);
        mTvSort = findViewById(R.id.tvSort_sort_filter);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_activity_device_list);
        mRecyclerView = findViewById(R.id.recyclerview_activity_device_list);
        mFlFilter = findViewById(R.id.flFilter_activity_device_list);

        mTvSort.setOnClickListener(this);
        findViewById(R.id.tvFilter_sort_filter).setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mItemList = new ArrayList<>();
        mListAdapter = new DeviceListAdapter(R.layout.item_recycler_device_list, mItemList);
        mListAdapter.setEnableLoadMore(true);
        mRecyclerView.setAdapter(mListAdapter);
        View headerView = View.inflate(this, R.layout.view_search, null);
        mEtSearch = headerView.findViewById(R.id.etSearch_view_search);
        mListAdapter.addHeaderView(headerView);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //执行搜索
                    getDeviceList(true, 1, mOrderBy, mFilterLocation, mFilterSystemCategory, mEtSearch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDeviceList(true, 1, mOrderBy, mFilterLocation, mFilterSystemCategory, mEtSearch.getText().toString().trim());
            }
        });;

        mListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDeviceList(false, mNextPage, mOrderBy, mFilterLocation, mFilterSystemCategory, mEtSearch.getText().toString().trim());
            }
        }, mRecyclerView);

        mListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DeviceObject deviceObject = mListAdapter.getData().get(position);
                Intent intent = new Intent();
                intent.putExtra("deviceName", deviceObject.ZC_MS);
                intent.putExtra("deviceId", deviceObject.ZC_BM);
                intent.putExtra("deviceLocation", deviceObject.WZ_BM_DES);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mDeviceFilterFragment = new DeviceFilterFragment();
        getFragmentManager().beginTransaction().add(R.id.flFilter_activity_device_list, mDeviceFilterFragment).commitAllowingStateLoss();

        mOrderBy = getResources().getStringArray(R.array.device_sort_value)[0];
        getDeviceList(true, 1, mOrderBy, mFilterLocation, mFilterSystemCategory, "");
    }

    private void getDeviceList(final boolean isRefresh, final int pageNo, String orderBy, String location, String systemCategory, String searchValue) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("wz", location);
        paramMap.put("zl", systemCategory);
        paramMap.put("key", searchValue);
        paramMap.put("pageNo", pageNo + "");
        paramMap.put("pageSize", "20");
        paramMap.put("orderBy", orderBy);
        OkgoRequest.excute(this, AppConstants.URL_GET_DEVICE, paramMap, new OkgoRequest.IResponseCallback() {
            @Override
            public void onResponse(String response) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (response != null) {
                    Gson gson = new Gson();
                    DeviceListResponseObject responseObj = gson.fromJson(response, DeviceListResponseObject.class);
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
                                mTvSort.setText(getResources().getStringArray(R.array.device_sort)[which]);
                                mOrderBy = getResources().getStringArray(R.array.device_sort_value)[which];
                                getDeviceList(true, 1, mOrderBy, mFilterLocation, mFilterSystemCategory, mEtSearch.getText().toString().trim());
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
            if (bundle.get("category").equals(AppConstants.TAG_DEVICE)) {
                mFlFilter.setVisibility(View.GONE);
                mFilterLocation = bundle.getString("location");
                mFilterSystemCategory = bundle.getString("system");
                getDeviceList(true, 1, mOrderBy, mFilterLocation, mFilterSystemCategory, mEtSearch.getText().toString().trim());
            }
        }
    }
}
