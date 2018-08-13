package com.cqsynet.ema.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cqsynet.ema.R;
import com.cqsynet.ema.adapter.TodoListFragmentAdapter;
import com.cqsynet.ema.fragment.TodoListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 待办任务
 */
public class TodoListActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todolist);
        mTabLayout = findViewById(R.id.tab_layout_activity_todolist);
        mViewPager = findViewById(R.id.view_pager_activity_todolist);
        ((TextView) findViewById(R.id.tvTitle_titlebar)).setText(R.string.todo_list);
        ImageButton ibtnBack = findViewById(R.id.ibtnLeft_titlebar);
        ibtnBack.setVisibility(View.VISIBLE);
        ibtnBack.setImageResource(R.drawable.btn_back_selector);
        ibtnBack.setOnClickListener(this);

        initViewPager();
    }

    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.todolist_no));
        titles.add(getString(R.string.todolist_yes));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.todolist_no));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.todolist_yes));
        List<Fragment> fragments = new ArrayList<>();
        TodoListFragment fragment1 = new TodoListFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("category", getString(R.string.todolist_no));
        fragment1.setArguments(bundle1);
        fragments.add(fragment1);
        TodoListFragment fragment2 = new TodoListFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("category", getString(R.string.todolist_yes));
        fragment2.setArguments(bundle2);
        fragments.add(fragment2);

        mViewPager.setOffscreenPageLimit(2);

        TodoListFragmentAdapter mFragmentAdapter = new TodoListFragmentAdapter(getFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(pageChangeListener);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {}

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnLeft_titlebar:
                finish();
                break;
        }
    }
}
