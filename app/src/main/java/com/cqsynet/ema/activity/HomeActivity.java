package com.cqsynet.ema.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cqsynet.ema.R;
import com.cqsynet.ema.adapter.HomeGridAdapter;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.db.AuthorityDao;
import com.cqsynet.ema.db.DictionaryDao;
import com.cqsynet.ema.model.AuthorityObject;
import com.cqsynet.ema.model.DictionaryResponseObject;
import com.cqsynet.ema.model.HomeGridObject;
import com.cqsynet.ema.network.OkgoRequest;
import com.cqsynet.ema.util.SharedPreferencesUtil;
import com.cqsynet.ema.view.GridDivider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
            return;
        }

        setContentView(R.layout.activity_home);

        TextView tvTitle = findViewById(R.id.tvTitle_titlebar);
        TextView tvLogout = findViewById(R.id.tvRight_titlebar);
        mRecyclerView = findViewById(R.id.recyclerview_activity_main);

        tvTitle.setText(R.string.home);
        tvLogout.setVisibility(View.VISIBLE);
        tvLogout.setText(R.string.quit);
        tvLogout.setOnClickListener(this);

        initRecyclerView();
        updateDictionary();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRight_titlebar:
                logout();
                onBackPressed();
                break;
        }
    }

    /**
     * 初始化recyclerView
     */
    private void initRecyclerView() {
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

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.addItemDecoration(new GridDivider(this, 1, getResources().getColor(R.color.divider), 2));
        HomeGridAdapter adapter = new HomeGridAdapter(R.layout.item_home_grid, mItemList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeGridObject object = mItemList.get(position);
                if(object.isShow.equals("0")) {
                    ToastUtils.showShort(R.string.no_authority);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this, MainActivity.class);
                    intent.putExtra("id", mItemList.get(position).id);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     * 更新数据字典
     */
    private void updateDictionary() {
        if(DictionaryDao.getInstance(this).getCount(AppConstants.DICTIONARY_TYPE_WORKORDER_PRIORITY) == 0) {
            getDictionary(AppConstants.DICTIONARY_TYPE_WORKORDER_PRIORITY);
        }
        if(DictionaryDao.getInstance(this).getCount(AppConstants.DICTIONARY_TYPE_WORKORDER_STATUS) == 0) {
            getDictionary(AppConstants.DICTIONARY_TYPE_WORKORDER_STATUS);
        }
    }

    /**
     * 从服务器获取字典表数据
     *
     * @param type 类型
     */
    private void getDictionary(String type) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("type", type);
        OkgoRequest.excute(this, AppConstants.URL_DICTIONARY, paramMap, new OkgoRequest.IResponseCallback() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Gson gson = new Gson();
                    DictionaryResponseObject responseObj = gson.fromJson(response, DictionaryResponseObject.class);
                    if (responseObj != null) {
                        if (AppConstants.RET_OK.equals(responseObj.ret)) {
                            DictionaryDao.getInstance(HomeActivity.this).saveDictionary(responseObj.data.data);
                        } else {
                            ToastUtils.showShort(responseObj.msg);
                        }
                    } else {
                        ToastUtils.showShort(R.string.request_failed);
                    }
                }
            }

            @Override
            public void onErrorResponse() {
                ToastUtils.showShort(R.string.request_failed);
            }
        });
    }

    private void logout() {
        OkgoRequest.excute(this, AppConstants.URL_LOGOUT, new HashMap(), new OkgoRequest.IResponseCallback() {
            @Override
            public void onResponse(String response) {
                SharedPreferencesUtil.removeData(HomeActivity.this, SharedPreferencesUtil.SEESION_ID);
            }

            @Override
            public void onErrorResponse() {
            }
        });
    }


}
