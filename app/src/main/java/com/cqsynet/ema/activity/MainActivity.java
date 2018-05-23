package com.cqsynet.ema.activity;

import android.app.Fragment;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.cqsynet.ema.R;
import com.cqsynet.ema.view.BottomNavigationViewHelper;

public class MainActivity extends BaseActivity {

    private BottomNavigationView mBottomNavi;
    private Fragment mCurrentFragment;
    private Fragment mNewsMainFragment;
    private Fragment mSocialFragment;
    private Fragment mMineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavi = findViewById(R.id.bottom_navigation_activity_main);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavi);

        setBottomNaviColor();

        mBottomNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_navigation_home:
                        if(mCurrentFragment != mNewsMainFragment) {
                            showHideFragment(mCurrentFragment, mNewsMainFragment);
                            mCurrentFragment = mNewsMainFragment;
                        }
                        return true;
                    case R.id.bottom_navigation_patrol:
                        if(mCurrentFragment != mSocialFragment) {
                            showHideFragment(mCurrentFragment, mSocialFragment);
                            mCurrentFragment = mSocialFragment;
                        }
                        return true;
                    case R.id.bottom_navigation_workorder:
                        if(mCurrentFragment != mMineFragment) {
                            showHideFragment(mCurrentFragment, mMineFragment);
                            mCurrentFragment = mMineFragment;
                        }
                        return true;
                    case R.id.bottom_navigation_kpi:
                        if(mCurrentFragment != mMineFragment) {
                            showHideFragment(mCurrentFragment, mMineFragment);
                            mCurrentFragment = mMineFragment;
                        }
                        return true;
                    case R.id.bottom_navigation_repair:
                        if(mCurrentFragment != mMineFragment) {
                            showHideFragment(mCurrentFragment, mMineFragment);
                            mCurrentFragment = mMineFragment;
                        }
                        return true;
                }
                return false;
            }
        });

//        mNewsMainFragment = new NewsMainFragment();
//        mSocialFragment = new WidgetsFragment();
//        mMineFragment = new MineFragment();
//        mCurrentFragment = mNewsMainFragment;
//        getFragmentManager()
//                .beginTransaction()
//                .add(R.id.flContainer_activity_main, mNewsMainFragment, mNewsMainFragment.getClass().getSimpleName())
//                .commitAllowingStateLoss();
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
//        FragmentManager fManager = getFragmentManager();
//        // 如果要隐藏的fragment非空，隐藏。
//        if (outFragment != null) {
//            fManager.beginTransaction()
//                    .hide(outFragment)
//                    .commit();
//        }
//        // 先从栈中看是否存在要显示的fagment。
//        String tag = inFragment.getClass().getSimpleName();
//        Fragment tempFragment = fManager.findFragmentByTag(tag);
//        if(tempFragment != null) { // 存在则直接显示。
//            fManager.beginTransaction()
//                    .show(inFragment)
//                    .commitAllowingStateLoss();
//        } else { // 不存在就添加并显示。
//            fManager.beginTransaction()
//                    .add(R.id.flContainer_activity_main, inFragment, tag)
//                    .addToBackStack(tag)
//                    .commitAllowingStateLoss();
//        }
    }
}
