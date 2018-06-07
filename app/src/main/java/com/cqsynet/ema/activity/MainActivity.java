package com.cqsynet.ema.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.blankj.utilcode.util.ToastUtils;
import com.cqsynet.ema.R;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.db.AuthorityDao;
import com.cqsynet.ema.fragment.KpiFragment;
import com.cqsynet.ema.fragment.PatrolFragment;
import com.cqsynet.ema.fragment.ReportFragment;
import com.cqsynet.ema.fragment.WorkOrderFragment;
import com.cqsynet.ema.model.AuthorityObject;
import com.cqsynet.ema.view.BottomNavigationViewHelper;

import java.util.ArrayList;

/**
 * 主Tab页
 */
public class MainActivity extends BaseActivity {

    private BottomNavigationView mBottomNavi;
    private Fragment mCurrentFragment;
    private Fragment mPatrolFragment;
    private Fragment mWorkOrderFragment;
    private Fragment mKpiFragment;
    private Fragment mReportFragment;
    private String mId;
    private ArrayList<AuthorityObject> mAuthorityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mId = getIntent().getStringExtra("id");

        mBottomNavi = findViewById(R.id.bottom_navigation_activity_main);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavi);
        setBottomNaviColor();

        mBottomNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(mAuthorityList == null) {
                    mAuthorityList = AuthorityDao.getInstance(MainActivity.this).queryAuthority();
                }
                switch (item.getItemId()) {
                    case R.id.bottom_navigation_home:
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.bottom_navigation_patrol:
                        for(AuthorityObject obj : mAuthorityList) {
                            if(obj.id.equals(AppConstants.ID_PATROL)) {
                                if(obj.isShow.equals("0")) {
                                    ToastUtils.showShort(R.string.no_authority);
                                    return false;
                                }
                            }
                        }
                        if(mCurrentFragment != mPatrolFragment) {
                            showHideFragment(mCurrentFragment, mPatrolFragment);
                            mCurrentFragment = mPatrolFragment;
                        }
                        break;
                    case R.id.bottom_navigation_workorder:
//                        for(AuthorityObject obj : mAuthorityList) {
//                            if(obj.id.equals(AppConstants.ID_WORKORDER)) {
//                                if(obj.isShow.equals("0")) {
//                                    ToastUtils.showShort(R.string.no_authority);
//                                    return false;
//                                }
//                            }
//                        }
                        if(mCurrentFragment != mWorkOrderFragment) {
                            showHideFragment(mCurrentFragment, mWorkOrderFragment);
                            mCurrentFragment = mWorkOrderFragment;
                        }
                        break;
                    case R.id.bottom_navigation_kpi:
                        for(AuthorityObject obj : mAuthorityList) {
                            if(obj.id.equals(AppConstants.ID_KPI)) {
                                if(obj.isShow.equals("0")) {
                                    ToastUtils.showShort(R.string.no_authority);
                                    return false;
                                }
                            }
                        }
                        if(mCurrentFragment != mKpiFragment) {
                            showHideFragment(mCurrentFragment, mKpiFragment);
                            mCurrentFragment = mKpiFragment;
                        }
                        break;
                    case R.id.bottom_navigation_report:
//                        for(AuthorityObject obj : mAuthorityList) {
//                            if(obj.id.equals(AppConstants.ID_REPORT)) {
//                                if(obj.isShow.equals("0")) {
//                                    ToastUtils.showShort(R.string.no_authority);
//                                    return false;
//                                }
//                            }
//                        }
                        if(mCurrentFragment != mReportFragment) {
                            showHideFragment(mCurrentFragment, mReportFragment);
                            mCurrentFragment = mReportFragment;
                        }
                        break;
                }
                return true;
            }
        });

        initFragment();
    }

    private void initFragment() {
        mPatrolFragment = new PatrolFragment();
        mWorkOrderFragment = new WorkOrderFragment();
        mKpiFragment = new KpiFragment();
        mReportFragment = new ReportFragment();

        if(AppConstants.ID_PATROL.equals(mId)) {
            mBottomNavi.setSelectedItemId(R.id.bottom_navigation_patrol);
            mCurrentFragment = mPatrolFragment;
        } else if (AppConstants.ID_WORKORDER.equals(mId)) {
            mBottomNavi.setSelectedItemId(R.id.bottom_navigation_workorder);
            mCurrentFragment = mWorkOrderFragment;
        }else if (AppConstants.ID_KPI.equals(mId)) {
            mBottomNavi.setSelectedItemId(R.id.bottom_navigation_kpi);
            mCurrentFragment = mKpiFragment;
        }else if (AppConstants.ID_REPORT.equals(mId)) {
            mBottomNavi.setSelectedItemId(R.id.bottom_navigation_report);
            mCurrentFragment = mReportFragment;
        }
    }

    /**
     * 设置底部导航栏颜色
     */
    private void setBottomNaviColor() {
        int[][] states = new int[][] {
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };

        int[] colors = new int[]{getResources().getColor(R.color.icon_gray),
                getResources().getColor(R.color.colorAccent)
        };
        ColorStateList csl = new ColorStateList(states, colors);
        mBottomNavi.setItemTextColor(csl);
        mBottomNavi.setItemIconTintList(csl);
    }


    /**
     * 切换模块
     * @param outFragment 隐藏的模块
     * @param inFragment 显示的模块
     * @param <T>
     */
    public <T extends Fragment> void showHideFragment(T outFragment, T inFragment) {
        FragmentManager fManager = getFragmentManager();
        // 如果要隐藏的fragment非空，隐藏。
        if (outFragment != null) {
            fManager.beginTransaction()
                    .hide(outFragment)
                    .commit();
        }
        // 先从栈中看是否存在要显示的fagment。
        String tag = inFragment.getClass().getSimpleName();
        Fragment tempFragment = fManager.findFragmentByTag(tag);
        if(tempFragment != null) { // 存在则直接显示。
            fManager.beginTransaction()
                    .show(inFragment)
                    .commitAllowingStateLoss();
        } else { // 不存在就添加并显示。
            fManager.beginTransaction()
                    .add(R.id.flContainer_activity_main, inFragment, tag)
//                    .addToBackStack(tag)
                    .commitAllowingStateLoss();
        }
    }
}
