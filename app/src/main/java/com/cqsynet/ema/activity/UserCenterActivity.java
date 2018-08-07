package com.cqsynet.ema.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cqsynet.ema.R;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.network.OkgoRequest;
import com.cqsynet.ema.util.SharedPreferencesUtil;

public class UserCenterActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);

        TextView tvTitle = findViewById(R.id.tvTitle_titlebar);
        ImageButton ibtnQuit = findViewById(R.id.ibtnLeft_titlebar);
        tvTitle.setText(R.string.user_center);
        ibtnQuit.setVisibility(View.VISIBLE);
        ibtnQuit.setImageResource(R.drawable.btn_back_selector);
        ibtnQuit.setOnClickListener(this);
        findViewById(R.id.btnLogout_activity_user_center).setOnClickListener(this);
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
}
