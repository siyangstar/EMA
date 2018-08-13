package com.cqsynet.ema.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.cqsynet.ema.R;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.common.Globals;
import com.cqsynet.ema.model.DepartmentObject;
import com.cqsynet.ema.network.OkgoRequest;
import com.cqsynet.ema.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class UserCenterActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvName;
    private TextView mTvNo;
    private TextView mTvDepartment;
    private ArrayList<String> mDepartmentNameList;
    private DepartmentObject mDepartmentObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);

        mTvName = findViewById(R.id.tvName_activity_user_center);
        mTvNo = findViewById(R.id.tvNo_activity_user_center);
        mTvDepartment = findViewById(R.id.tvDepartment_activity_user_center);
        TextView tvTitle = findViewById(R.id.tvTitle_titlebar);
        ImageButton ibtnQuit = findViewById(R.id.ibtnLeft_titlebar);
        tvTitle.setText(R.string.user_center);
        ibtnQuit.setVisibility(View.VISIBLE);
        ibtnQuit.setImageResource(R.drawable.btn_back_selector);
        ibtnQuit.setOnClickListener(this);
        findViewById(R.id.btnLogout_activity_user_center).setOnClickListener(this);
        findViewById(R.id.llDepartment_activity_user_center).setOnClickListener(this);

        if (Globals.g_UserInfo != null) {
            mTvName.setText(Globals.g_UserInfo.name);
            mTvNo.setText(Globals.g_UserInfo.no);
            if (Globals.g_UserInfo.office != null) {
                mTvDepartment.setText(Globals.g_UserInfo.office.name);
            }
        }

        mDepartmentNameList = new ArrayList<>();
        Iterator<DepartmentObject> iterator = Globals.g_UserInfo.officeList.iterator();
        while(iterator.hasNext()) {
            DepartmentObject object = iterator.next();
            mDepartmentNameList.add(object.name);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnLeft_titlebar:
                finish();
                break;
            case R.id.btnLogout_activity_user_center:
                logout();
                break;
            case R.id.llDepartment_activity_user_center:
                if (mDepartmentNameList.size() == 0) {
                    ToastUtils.showShort("无所属部门");
                } else {
                    new MaterialDialog.Builder(this)
                            .items(mDepartmentNameList)
                            .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                    mDepartmentObject = Globals.g_UserInfo.officeList.get(which);
                                    if (mDepartmentObject != null) {
                                        mTvDepartment.setText(mDepartmentObject.name);
                                    }
                                    Globals.g_UserInfo.office = mDepartmentObject;
                                    saveDepartment();
                                    return true;
                                }
                            })
                            .positiveText("保存")
                            .show();
                }
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logout() {
        OkgoRequest.excute(this, AppConstants.URL_LOGOUT, null, new OkgoRequest.IResponseCallback() {
            @Override
            public void onResponse(String response) {
                SharedPreferencesUtil.removeData(UserCenterActivity.this, SharedPreferencesUtil.SEESION_ID);
                Intent intent = new Intent(UserCenterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onErrorResponse() {
            }
        });
    }

    /**
     * 保存用户部门
     */
    private void saveDepartment() {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("office", mDepartmentObject.id);
        paramMap.put("isDefult", "1"); //设为默认
        OkgoRequest.excute(this, AppConstants.URL_SAVE_USER_DEPARTMENT, paramMap, new OkgoRequest.IResponseCallback() {
            @Override
            public void onResponse(String response) {
            }

            @Override
            public void onErrorResponse() {
            }
        });
    }

}
