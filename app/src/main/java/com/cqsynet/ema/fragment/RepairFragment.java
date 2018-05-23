package com.cqsynet.ema.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cqsynet.ema.R;

public class RepairFragment extends Fragment implements View.OnClickListener {

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
                break;
            case R.id.tvFilter_sort_filter:
                break;
        }
    }
}
