package com.cqsynet.ema.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cqsynet.ema.R;
import com.cqsynet.ema.adapter.HomeGridAdapter;
import com.cqsynet.ema.model.HomeGridObject;
import com.cqsynet.ema.view.GridDivider;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ArrayList<HomeGridObject> mItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView tvTitle = findViewById(R.id.tvTitle_titlebar);
        ImageButton ibtnQuit = findViewById(R.id.ibtnLeft_titlebar);
        mRecyclerView = findViewById(R.id.recyclerview_activity_main);

        tvTitle.setText(R.string.home);
        ibtnQuit.setVisibility(View.VISIBLE);
        ibtnQuit.setImageResource(R.drawable.btn_back_selector);
        ibtnQuit.setOnClickListener(this);

        initData();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.addItemDecoration(new GridDivider(this, 1, getResources().getColor(R.color.divider), 2));
        HomeGridAdapter adapter = new HomeGridAdapter(R.layout.item_home_grid, mItemList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnLeft_titlebar:
                onBackPressed();
                break;
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mItemList = new ArrayList<>();
        String[] titleAry = {getString(R.string.todo_list), getString(R.string.patrol), getString(R.string.work_order), getString(R.string.kpi), getString(R.string.repair)};
        int[] iconAry = {R.drawable.home_todolist, R.drawable.home_patrol, R.drawable.home_workorder, R.drawable.home_kpi, R.drawable.home_repair};
        for(int i = 0; i < titleAry.length; i++) {
            HomeGridObject obj = new HomeGridObject();
            obj.title = titleAry[i];
            obj.imageRes = iconAry[i];
            mItemList.add(obj);
        }
    }

}
