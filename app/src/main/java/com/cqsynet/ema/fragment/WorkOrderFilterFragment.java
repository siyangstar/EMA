package com.cqsynet.ema.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cqsynet.ema.R;
import com.cqsynet.ema.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * 筛选模块
 */
public class WorkOrderFilterFragment extends BaseFragment implements View.OnClickListener {

    private TextView mTvStartDate;
    private TextView mTvEndDate;
    private TextView mTvStatus;
    private CheckBox mCbOnlyMine;
    private MaterialDialog mDateDialog;
    private DatePicker mDatePicker;
    private int mDateType; //0表示开始日期,1表示结束日期

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workorder_filter, container, false);
        view.findViewById(R.id.btnCancel_fragment_workorder_filter).setOnClickListener(this);
        view.findViewById(R.id.btnConfirm_fragment_workorder_filter).setOnClickListener(this);
        mTvStartDate = view.findViewById(R.id.tvStartDate_fragment_workorder_filter);
        mTvEndDate = view.findViewById(R.id.tvEndDate_fragment_workorder_filter);
        mTvStatus = view.findViewById(R.id.btnCancel_fragment_workorder_filter);
        mCbOnlyMine = view.findViewById(R.id.cbOnlyMine_fragment_workorder_filter);
        mTvStartDate.setOnClickListener(this);
        mTvEndDate.setOnClickListener(this);
        mTvStatus.setOnClickListener(this);

        mDateDialog = new MaterialDialog.Builder(mContext).customView(R.layout.dialog_date_picker, false).build();
        mDatePicker = (DatePicker) mDateDialog.getCustomView();
        Calendar calendar = Calendar.getInstance();
        mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDateDialog.dismiss();
                if(mDateType == 0) {
                    mTvStartDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                } else {
                    mTvEndDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel_fragment_workorder_filter:
                Bundle bundleCancel = new Bundle();
                bundleCancel.putString("type", "cancelFilter");
                EventBus.getDefault().post(new MessageEvent(bundleCancel));
                break;
            case R.id.btnConfirm_fragment_workorder_filter:
                Bundle bundleConfirm = new Bundle();
                bundleConfirm.putString("type", "confirmFilter");
                bundleConfirm.putString("startDate", mTvStartDate.getText().toString().trim());
                bundleConfirm.putString("endDate", mTvEndDate.getText().toString().trim());
                bundleConfirm.putString("status", mTvStatus.getText().toString().trim());
                bundleConfirm.putBoolean("onlyMine", mCbOnlyMine.isChecked());
                EventBus.getDefault().post(new MessageEvent(bundleConfirm));
                break;
            case R.id.tvStartDate_fragment_workorder_filter:
                mDateType = 0;
                mDateDialog.show();
                break;
            case R.id.tvEndDate_fragment_workorder_filter:
                mDateType = 1;
                mDateDialog.show();
                break;
        }
    }


}
