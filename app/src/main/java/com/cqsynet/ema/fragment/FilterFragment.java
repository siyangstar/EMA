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
import com.cqsynet.ema.model.LocationObject;
import com.cqsynet.ema.model.MessageEvent;
import com.cqsynet.ema.model.SystemObject;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends BaseFragment implements View.OnClickListener {

    private RadioGroup mRadioGroup;
    private ViewPager mVpLocation;
    private ViewPager mVpSystem;
    private FilterViewPagerAdapter mLocationPagerAdapter;
    private FilterViewPagerAdapter mSystemPagerAdapter;
    private ArrayList<RecyclerView> mLocationRecyclerList;
    private ArrayList<RecyclerView> mSystemRecyclerList;
    private ArrayList<LocationObject> mLocationLevelOneList;
    private ArrayList<SystemObject> mSystemLevelOneList;
    private LocationObject mSelectedLocationObject; //选择的位置条件
    private SystemObject mSelectedSystemObject;  //选择的系统条件

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationRecyclerList = new ArrayList<>();
        mSystemRecyclerList = new ArrayList<>();

        mLocationLevelOneList = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            LocationObject obj = new LocationObject();
            obj.WZ_MS = "四川";
            obj.WZ_BM = "00" + i;
            obj.F_WZ_BM = "";
            mLocationLevelOneList.add(obj);
        }

        mSystemLevelOneList = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            SystemObject obj = new SystemObject();
            obj.ZL_MS = "路由器";
            obj.ZL_BM = "00" + i;
            obj.F_ZL_BM = "";
            mSystemLevelOneList.add(obj);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        mRadioGroup = view.findViewById(R.id.rg_fragment_filter);
        mVpLocation = view.findViewById(R.id.vpLocation_fragment_filter);
        mVpSystem = view.findViewById(R.id.vpSystem_fragment_filter);

        view.findViewById(R.id.btnCancel_fragment_filter).setOnClickListener(this);
        view.findViewById(R.id.btnConfirm_fragment_filter).setOnClickListener(this);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLocation_fragment_filter:
                        mVpLocation.setVisibility(View.VISIBLE);
                        mVpSystem.setVisibility(View.GONE);
                        break;
                    case R.id.rbSystem_fragment_filter:
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
                    ArrayList<LocationObject> locationList = getLocationList(parentId);
                    updateLocationViewPager(locationList);
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
    private void updateSystemViewPager(ArrayList<SystemObject> list) {
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
                SystemObject systemObject = (SystemObject) adapter.getItem(position);
                if(systemObject != null) {
                    String parentId = systemObject.ZL_BM;
                    ArrayList<SystemObject> systemList = getSystemList(parentId);
                    updateSystemViewPager(systemList);
                }
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                boolean isChecked = ((CheckBox) view).isChecked();
                SystemObject systemObject = (SystemObject) adapter.getItem(position);
                if(systemObject != null) {
                    if (isChecked) {
                        systemObject.selected = true;
                        mSelectedSystemObject = systemObject;
                        updateCheckBox("system");
                    } else {
                        systemObject.selected = false;
                        mSelectedSystemObject = null;
                    }
                }
            }
        });

        mSystemRecyclerList.add(recyclerView);
        mSystemPagerAdapter.notifyDataSetChanged();
        mVpSystem.setCurrentItem(mSystemPagerAdapter.getCount() - 1);
    }

    /**
     * 查询位置列表
     * @param parentId
     * @return
     */
    private ArrayList<LocationObject> getLocationList(String parentId) {
        ArrayList<LocationObject> locationList = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            LocationObject obj = new LocationObject();
            obj.WZ_MS = "成都";
            obj.WZ_BM = "00100" + i;
            obj.F_WZ_BM = "001";
            locationList.add(obj);
        }
        return locationList;
    }

    /**
     * 查询系统列表
     * @param parentId
     * @return
     */
    private ArrayList<SystemObject> getSystemList(String parentId) {
        ArrayList<SystemObject> systemList = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            SystemObject obj = new SystemObject();
            obj.ZL_MS = "机柜";
            obj.ZL_BM = "00100" + i;
            obj.F_ZL_BM = "001";
            systemList.add(obj);
        }
        return systemList;
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
                List<SystemObject> list = adapter.getData();
                for(SystemObject systemObject : list) {
                    if(!systemObject.ZL_BM.equals(mSelectedSystemObject.ZL_BM)) {
                        systemObject.selected = false;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}
