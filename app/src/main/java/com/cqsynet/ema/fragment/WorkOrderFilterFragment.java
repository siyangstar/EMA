package com.cqsynet.ema.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cqsynet.ema.R;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.db.DictionaryDao;
import com.cqsynet.ema.model.DictionaryObject;
import com.cqsynet.ema.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

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
    private String mCategory;
    private ArrayList<String> mLabelList;
    private ArrayList<String> mIdList;
    private String mStatusValue = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = getArguments().getString("category");

        ArrayList<DictionaryObject> list =  DictionaryDao.getInstance(mContext).queryDictionaryList("process", "470785ea796e477fb894eefd87164234"); //工单的流程状态类别id
        mLabelList = new ArrayList<>();
        mLabelList.add("不限");
        mIdList = new ArrayList<>();
        mIdList.add("");
        Iterator<DictionaryObject> iterator = list.iterator();
        while (iterator.hasNext()) {
            DictionaryObject object = iterator.next();
            mLabelList.add(object.label);
            mIdList.add(object.value);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workorder_filter, container, false);
        view.findViewById(R.id.btnCancel_fragment_workorder_filter).setOnClickListener(this);
        view.findViewById(R.id.btnConfirm_fragment_workorder_filter).setOnClickListener(this);
        mTvStartDate = view.findViewById(R.id.tvStartDate_fragment_workorder_filter);
        mTvEndDate = view.findViewById(R.id.tvEndDate_fragment_workorder_filter);
        mTvStatus = view.findViewById(R.id.tvStatus_fragment_workorder_filter);
        mCbOnlyMine = view.findViewById(R.id.cbOnlyMine_fragment_workorder_filter);
        if(mCategory.equals(AppConstants.TAG_WORK_ORDER)) {
            view.findViewById(R.id.flOnlyMine_fragment_workorder_filter).setVisibility(View.INVISIBLE);
        }
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
                    mTvStartDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                } else {
                    mTvEndDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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
                bundleConfirm.putString("category", mCategory);
                bundleConfirm.putString("startDate", mTvStartDate.getText().toString().trim());
                bundleConfirm.putString("endDate", mTvEndDate.getText().toString().trim());
                bundleConfirm.putString("status", mStatusValue);
                bundleConfirm.putString("onlyMine", mCbOnlyMine.isChecked() ? "myDataUserId" : "");
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
            case R.id.tvStatus_fragment_workorder_filter:
                new MaterialDialog.Builder(mContext)
                        .dividerColorRes(R.color.divider)
                        .itemsGravity(GravityEnum.CENTER)
                        .items(mLabelList)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                mTvStatus.setText(mLabelList.get(which));
                                if(mIdList.get(which).split("@@@@").length > 1) {
                                    mStatusValue = mIdList.get(which).split("@@@@")[1]; //数据库中使用@@@@连接一级id和二级id
                                } else {
                                    mStatusValue = mIdList.get(which);
                                }
                            }
                        })
                        .show();
                break;
        }
    }


}
