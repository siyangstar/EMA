package com.cqsynet.ema.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.cqsynet.ema.R;
import com.cqsynet.ema.activity.SubmitReportActivity;
import com.cqsynet.ema.model.MessageEvent;

public class ReportFragment extends BaseFragment implements View.OnClickListener {

    private FrameLayout mFlContainer;
    private Fragment mFilterFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        TextView tvTitle = view.findViewById(R.id.tvTitle_titlebar);
        TextView tvRight = view.findViewById(R.id.tvRight_titlebar);
        mFlContainer = view.findViewById(R.id.flContainer_fragment_report);

        tvTitle.setText(R.string.report);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.submit_report);

        tvRight.setOnClickListener(this);
        view.findViewById(R.id.tvSort_sort_filter).setOnClickListener(this);
        view.findViewById(R.id.tvFilter_sort_filter).setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFilterFragment = new FilterFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSort_sort_filter:
                new MaterialDialog.Builder(mContext)
                        .titleGravity(GravityEnum.CENTER)
                        .title(R.string.equip_sort)
                        .dividerColorRes(R.color.divider)
                        .itemsGravity(GravityEnum.CENTER)
                        .items(R.array.equipment_sort)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                ToastUtils.showShort(text);
                            }
                        })
                        .show();
                break;
            case R.id.tvFilter_sort_filter:
                switchFilter();
                break;
            case R.id.tvRight_titlebar:
                Intent intent = new Intent();
                intent.setClass(mContext, SubmitReportActivity.class);
                startActivity(intent);
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
            mFlContainer.setVisibility(View.GONE);
        } else if (type.equals("confirmFilter")) {
            mFlContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 显示/隐藏 筛选界面
     *
     */
    public <T extends Fragment> void switchFilter() {
        if(mFlContainer.getVisibility() == View.VISIBLE) {
            mFlContainer.setVisibility(View.GONE);
        } else {
            FragmentManager fm = getFragmentManager();
            String tag = mFilterFragment.getClass().getSimpleName();
            Fragment tempFragment = fm.findFragmentByTag(tag);
            if(tempFragment == null) { // 存在则直接显示。
                 fm.beginTransaction()
                        .add(R.id.flContainer_fragment_report, mFilterFragment, tag)
                        .commitAllowingStateLoss();
            }
            mFlContainer.setVisibility(View.VISIBLE);
        }
    }
}
