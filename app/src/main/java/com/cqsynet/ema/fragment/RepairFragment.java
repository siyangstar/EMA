package com.cqsynet.ema.fragment;

import android.content.Context;
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
import com.cqsynet.ema.model.MessageEvent;

public class RepairFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private FrameLayout mFlContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repair, container, false);
        TextView tvTitle = view.findViewById(R.id.tvTitle_titlebar);
        TextView tvRight = view.findViewById(R.id.tvRight_titlebar);
        mFlContainer = view.findViewById(R.id.flContainer_fragment_repair);

        tvTitle.setText(R.string.repair);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.request_for_repair);

        view.findViewById(R.id.tvSort_sort_filter).setOnClickListener(this);
        view.findViewById(R.id.tvFilter_sort_filter).setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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
                        .items(R.array.sort)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                ToastUtils.showShort(text);
                            }
                        })
                        .show();
                break;
            case R.id.tvFilter_sort_filter:
                break;
        }
    }

    public void onMessageEvent(MessageEvent messageEvent) {
        super.onMessageEvent(messageEvent);
    }
}
