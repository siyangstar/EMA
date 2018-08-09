package com.cqsynet.ema.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cqsynet.ema.R;
import com.cqsynet.ema.adapter.FilterViewPagerAdapter;
import com.cqsynet.ema.adapter.LocationRecyclerAdapter;
import com.cqsynet.ema.adapter.SystemRecyclerAdapter;
import com.cqsynet.ema.db.LocationDao;
import com.cqsynet.ema.db.SystemCategoryDao;
import com.cqsynet.ema.model.LocationObject;
import com.cqsynet.ema.model.MessageEvent;
import com.cqsynet.ema.model.SystemCategoryObject;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选模块
 */
public class EquipmentFilterFragment extends BaseFragment implements View.OnClickListener {

    private RadioGroup mRadioGroup;
    private ViewPager mVpLocation;
    private ViewPager mVpSystem;
    private FilterViewPagerAdapter mLocationPagerAdapter;
    private FilterViewPagerAdapter mSystemPagerAdapter;
    private ArrayList<RecyclerView> mLocationRecyclerList;
    private ArrayList<RecyclerView> mSystemRecyclerList;
    private ArrayList<LocationObject> mLocationLevelOneList;
    private ArrayList<SystemCategoryObject> mSystemLevelOneList;
    private LocationObject mSelectedLocationObject; //选择的位置条件
    private SystemCategoryObject mSelectedSystemCategoryObject;  //选择的系统条件

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationRecyclerList = new ArrayList<>();
        mSystemRecyclerList = new ArrayList<>();

        mLocationLevelOneList = new ArrayList<>();
        mLocationLevelOneList.addAll(LocationDao.getInstance(mContext).queryLocation(""));
        mSystemLevelOneList = new ArrayList<>();
        mSystemLevelOneList.addAll(SystemCategoryDao.getInstance(mContext).querySystemCategory(""));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipment_filter, container, false);
        mRadioGroup = view.findViewById(R.id.rg_fragment_equipment_filter);
        mVpLocation = view.findViewById(R.id.vpLocation_fragment_equipment_filter);
        mVpSystem = view.findViewById(R.id.vpSystem_fragment_equipment_filter);

        view.findViewById(R.id.btnCancel_fragment_equipment_filter).setOnClickListener(this);
        view.findViewById(R.id.btnConfirm_fragment_equipment_filter).setOnClickListener(this);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLocation_fragment_equipment_filter:
                        mVpLocation.setVisibility(View.VISIBLE);
                        mVpSystem.setVisibility(View.GONE);
                        break;
                    case R.id.rbSystem_fragment_equipment_filter:
                        mVpLocation.setVisibility(View.GONE);
                        mVpSystem.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        mLocationPagerAdapter = new FilterViewPagerAdapter(mLocationRecyclerList);
        mVpLocation.setAdapter(mLocationPagerAdapter);
        updateLocationViewPager(mLocationLevelOneList);

        mSystemPagerAdapter = new FilterViewPagerAdapter(mSystemRecyclerList);
        mVpSystem.setAdapter(mSystemPagerAdapter);
        updateSystemViewPager(mSystemLevelOneList);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel_fragment_equipment_filter:
                Bundle bundleCancel = new Bundle();
                bundleCancel.putString("type", "cancelFilter");
                EventBus.getDefault().post(new MessageEvent(bundleCancel));
                break;
            case R.id.btnConfirm_fragment_equipment_filter:
                Bundle bundleConfirm = new Bundle();
                bundleConfirm.putString("type", "confirmFilter");
                bundleConfirm.putString("location", mSelectedLocationObject != null ? mSelectedLocationObject.WZ_BM : "");
                bundleConfirm.putString("system", mSelectedSystemCategoryObject != null ? mSelectedSystemCategoryObject.ZL_BM : "");
                EventBus.getDefault().post(new MessageEvent(bundleConfirm));
                break;
        }
    }

    /**
     * 更新位置viewPager里面的列表
     * @param list 某一层级的位置列表
     */
    private void updateLocationViewPager(ArrayList<LocationObject> list) {
        LocationRecyclerAdapter adapter = new LocationRecyclerAdapter(R.layout.item_recycler_filter, list);
        final RecyclerView recyclerView = (RecyclerView) View.inflate(mContext, R.layout.filter_recycler_view, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击某一层时,清空它后面的所有子层级
                for(int index = 0; index < mLocationRecyclerList.size(); index++) {
                    if(recyclerView.equals(mLocationRecyclerList.get(index))) {
                        mLocationRecyclerList.subList(index + 1, mLocationRecyclerList.size()).clear();
                        break;
                    }
                }
                LocationObject locationObject = (LocationObject) adapter.getItem(position);
                if(locationObject != null) {
                    String parentId = locationObject.WZ_BM;
                    ArrayList<LocationObject> locationList = LocationDao.getInstance(mContext).queryLocation(parentId);
                    if(locationList.size() != 0) {
                        updateLocationViewPager(locationList);
                    }
                }
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                boolean isChecked = ((CheckBox) view).isChecked();
                LocationObject locationObject = (LocationObject) adapter.getItem(position);
                if(locationObject != null) {
                    if (isChecked) {
                        locationObject.selected = true;
                        mSelectedLocationObject = locationObject;
                        updateCheckBox("location");
                    } else {
                        locationObject.selected = false;
                        mSelectedLocationObject = null;
                    }
                }
            }
        });

        mLocationRecyclerList.add(recyclerView);
        mLocationPagerAdapter.notifyDataSetChanged();
        mVpLocation.setCurrentItem(mLocationPagerAdapter.getCount() - 1);
    }

    /**
     * 更新位置viewPager里面的列表
     * @param list 某一层级的位置列表
     */
    private void updateSystemViewPager(ArrayList<SystemCategoryObject> list) {
        SystemRecyclerAdapter adapter = new SystemRecyclerAdapter(R.layout.item_recycler_filter, list);
        final RecyclerView recyclerView = (RecyclerView) View.inflate(mContext, R.layout.filter_recycler_view, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击某一层时,清空它后面的所有子层级
                for(int index = 0; index < mSystemRecyclerList.size(); index++) {
                    if(recyclerView.equals(mSystemRecyclerList.get(index))) {
                        mSystemRecyclerList.subList(index + 1, mSystemRecyclerList.size()).clear();
                        break;
                    }
                }
                SystemCategoryObject systemCategoryObject = (SystemCategoryObject) adapter.getItem(position);
                if(systemCategoryObject != null) {
                    String parentId = systemCategoryObject.ZL_BM;
                    ArrayList<SystemCategoryObject> systemList = SystemCategoryDao.getInstance(mContext).querySystemCategory(parentId);
                    if(systemList.size() != 0) {
                        updateSystemViewPager(systemList);
                    }
                }
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                boolean isChecked = ((CheckBox) view).isChecked();
                SystemCategoryObject systemCategoryObject = (SystemCategoryObject) adapter.getItem(position);
                if(systemCategoryObject != null) {
                    if (isChecked) {
                        systemCategoryObject.selected = true;
                        mSelectedSystemCategoryObject = systemCategoryObject;
                        updateCheckBox("system");
                    } else {
                        systemCategoryObject.selected = false;
                        mSelectedSystemCategoryObject = null;
                    }
                }
            }
        });

        mSystemRecyclerList.add(recyclerView);
        mSystemPagerAdapter.notifyDataSetChanged();
        mVpSystem.setCurrentItem(mSystemPagerAdapter.getCount() - 1);
    }

    /**
     * 刷新checkbox勾选状态
     * @param type location:位置 system:系统
     */
    private void updateCheckBox(String type) {
        if(type.equals("location")) {
            for (RecyclerView recyclerView : mLocationRecyclerList) {
                LocationRecyclerAdapter adapter = (LocationRecyclerAdapter)recyclerView.getAdapter();
                List<LocationObject> list = adapter.getData();
                for(LocationObject locationObject : list) {
                    if(!locationObject.WZ_BM.equals(mSelectedLocationObject.WZ_BM)) {
                        locationObject.selected = false;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        } else if(type.equals("system")) {
            for (RecyclerView recyclerView : mSystemRecyclerList) {
                SystemRecyclerAdapter adapter = (SystemRecyclerAdapter)recyclerView.getAdapter();
                List<SystemCategoryObject> list = adapter.getData();
                for(SystemCategoryObject systemCategoryObject : list) {
                    if(!systemCategoryObject.ZL_BM.equals(mSelectedSystemCategoryObject.ZL_BM)) {
                        systemCategoryObject.selected = false;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}
