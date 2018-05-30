package com.cqsynet.ema.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cqsynet.ema.R;
import com.cqsynet.ema.adapter.HomeGridAdapter;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.db.AuthorityDao;
import com.cqsynet.ema.model.AuthorityObject;
import com.cqsynet.ema.model.HomeGridObject;
import com.cqsynet.ema.util.SharedPreferencesUtil;
import com.cqsynet.ema.view.GridDivider;

import java.util.ArrayList;
import java.util.Iterator;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ArrayList<HomeGridObject> mItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //是否有已登录,若未登录,跳转到登录界面
        if (TextUtils.isEmpty(SharedPreferencesUtil.getTagString(this, SharedPreferencesUtil.SEESION_ID))) {
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

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
                intent.putExtra("id", mItemList.get(position).id);
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
        ArrayList<AuthorityObject> list = AuthorityDao.getInstance(this).queryAuthority();
        mItemList = new ArrayList<>();
        Iterator<AuthorityObject> iter = list.iterator();
        while (iter.hasNext()) {
            AuthorityObject author = iter.next();
            if(author.id.equals("3da97fde4f2443b180bc9c1e9237d766")) {
                //跳过"首页"
                continue;
            }
            HomeGridObject obj = new HomeGridObject();
            obj.id = author.id;
            obj.title = author.name;
            obj.imageRes = AppConstants.ICON_MAP.get(author.id);
            obj.isShow = author.isShow;
            mItemList.add(obj);
        }
    }

}
