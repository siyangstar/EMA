package com.cqsynet.ema.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cqsynet.ema.R;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.model.MessageEvent;

/**
 * 工单模块
 */
public class WorkOrderFragment extends BaseFragment implements View.OnClickListener {

    private FrameLayout mFlFilter;
    private WorkOrderFilterFragment mFilterFragment;
    private WorkOrderListFragment mListFragment;
    private TextView mTvSort;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workorder, container, false);
        ((TextView) view.findViewById(R.id.tvTitle_titlebar)).setText(R.string.work_order);
        mTvSort = view.findViewById(R.id.tvSort_sort_filter);
        mFlFilter = view.findViewById(R.id.flFilter_fragment_workorder);
        mTvSort.setOnClickListener(this);
        view.findViewById(R.id.tvFilter_sort_filter).setOnClickListener(this);

        getFragmentManager().beginTransaction().add(R.id.flRecyclerView_fragment_workorder, mListFragment).commitAllowingStateLoss();
        getFragmentManager().beginTransaction().add(R.id.flFilter_fragment_workorder, mFilterFragment).commitAllowingStateLoss();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFilterFragment = new WorkOrderFilterFragment();
        mListFragment = new WorkOrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category", AppConstants.TAG_WORK_ORDER);
        mListFragment.setArguments(bundle);
        mFilterFragment.setArguments(bundle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSort_sort_filter:
                new MaterialDialog.Builder(mContext)
                        .titleGravity(GravityEnum.CENTER)
                        .title(R.string.sort)
                        .dividerColorRes(R.color.divider)
                        .itemsGravity(GravityEnum.CENTER)
                        .items(R.array.workorder_sort)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                mTvSort.setText(getResources().getStringArray(R.array.workorder_sort)[which]);
                                String value = getResources().getStringArray(R.array.workorder_sort_value)[which];
                                mListFragment.setOrderByParam(value);
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
            if (bundle.getString("category").equals(AppConstants.TAG_WORK_ORDER)) {
                mFlFilter.setVisibility(View.GONE);
                mListFragment.setFilter(bundle.getString("startDate"), bundle.getString("endDate"), bundle.getString("status"), bundle.getString("onlyMine"));
            }
        }
    }

    /**
     * 显示/隐藏 筛选界面
     *
     */
    public <T extends Fragment> void switchFilter() {
        if (mFlFilter.getVisibility() == View.VISIBLE) {
            mFlFilter.setVisibility(View.GONE);
        } else {
            mFlFilter.setVisibility(View.VISIBLE);
        }
    }
}
