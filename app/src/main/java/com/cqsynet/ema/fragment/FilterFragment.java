package com.cqsynet.ema.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cqsynet.ema.R;
import com.cqsynet.ema.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class FilterFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        view.findViewById(R.id.btnCancel_fragment_filter).setOnClickListener(this);
        view.findViewById(R.id.btnConfirm_fragment_filter).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel_fragment_filter:
                Bundle bundleCancel = new Bundle();
                bundleCancel.putString("type", "cancelFilter");
                EventBus.getDefault().post(new MessageEvent(bundleCancel));
                break;
            case R.id.btnConfirm_fragment_filter:
                Bundle bundleConfirm = new Bundle();
                bundleConfirm.putString("type", "confirmFilter");
                EventBus.getDefault().post(new MessageEvent(bundleConfirm));
                break;
        }
    }
}
