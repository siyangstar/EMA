package com.cqsynet.ema.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cqsynet.ema.R;

public class PatrolFragment extends BaseFragment implements View.OnClickListener {

    private FrameLayout mFlContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patrol, container, false);
        TextView tvTitle = view.findViewById(R.id.tvTitle_titlebar);
        mFlContainer = view.findViewById(R.id.flContainer_fragment_patrol);

        tvTitle.setText(R.string.patrol);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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