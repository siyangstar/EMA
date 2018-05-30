package com.cqsynet.ema.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cqsynet.ema.R;

public class ForgetPswActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);

        TextView tvTitle = findViewById(R.id.tvTitle_titlebar);
        ImageButton ibtnBack = findViewById(R.id.ibtnLeft_titlebar);

        tvTitle.setText(R.string.find_password);
        ibtnBack.setVisibility(View.VISIBLE);
        ibtnBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnLeft_titlebar:
                onBackPressed();
                break;
        }
    }
}
