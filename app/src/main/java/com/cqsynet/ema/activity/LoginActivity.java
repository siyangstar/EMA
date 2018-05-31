package com.cqsynet.ema.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.cqsynet.ema.R;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.db.AuthorityDao;
import com.cqsynet.ema.model.LoginResponseObject;
import com.cqsynet.ema.network.OkgoRequest;
import com.cqsynet.ema.network.OkgoRequest.IResponseCallback;
import com.cqsynet.ema.util.SharedPreferencesUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextInputLayout mTilUsername;
    private TextInputLayout mTilPassword;
    private AutoCompleteTextView mAtvUsername;
    private EditText mEtPassword;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    public void initView() {
        mAtvUsername = findViewById(R.id.tvUsername_activity_login);
        mEtPassword = findViewById(R.id.tvPassword_activity_login);
        mTilUsername = findViewById(R.id.tilUsername_activity_login);
        mTilPassword = findViewById(R.id.tilPassword_activity_login);

        mBtnLogin = findViewById(R.id.btnLogin_activity_login);
        mBtnLogin.setOnClickListener(this);
        findViewById(R.id.btnForgotPassword_activity_login).setOnClickListener(this);

        mAtvUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    checkUsername();
                }
            }
        });

        mEtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    checkPassword();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin_activity_login:
                attemptLogin();
//                login("18680933386", "111111");
                break;
            case R.id.btnForgotPassword_activity_login:
                Intent intent = new Intent(this, ForgetPswActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 校验用户名
     *
     * @return
     */
    private boolean checkUsername() {
        mTilUsername.setError(null);
        String userName = mAtvUsername.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            mTilUsername.setError(getString(R.string.username_empty));
            return false;
        }
        return true;

    }

    /**
     * 校验密码
     *
     * @return
     */
    private boolean checkPassword() {
        mTilPassword.setError(null);
        String password = mEtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mTilPassword.setError(getString(R.string.password_empty));
            return false;
        }
        return true;

    }

    /**
     * 发起登录
     */
    private void attemptLogin() {
        if (checkUsername() && checkPassword()) {
            hideInput(mBtnLogin);
            login(mAtvUsername.getText().toString(), mEtPassword.getText().toString());
        }
    }

    public void hideInput(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * @param username 要登陆的用户名
     * @param psw      登陆密码
     * @Description: 调用登陆接口发起登陆，并处理服务器返回信息
     */
    private void login(String username, String psw) {
        mProgressDialog.setMessage(getString(R.string.logining));
        mProgressDialog.show();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("username", username);
        paramMap.put("password", psw);
        paramMap.put("mobileLogin", "true");

        OkgoRequest.excute(this, AppConstants.URL_LOGIN, paramMap, new IResponseCallback() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Gson gson = new Gson();
                    LoginResponseObject responseObj = gson.fromJson(response, LoginResponseObject.class);
                    if (responseObj != null) {
                        if (AppConstants.RET_OK.equals(responseObj.ret)) {
                            SharedPreferencesUtil.setTagString(LoginActivity.this, SharedPreferencesUtil.SEESION_ID, responseObj.data.sessionid);
                            AuthorityDao.getInstance(LoginActivity.this).saveAuthority(responseObj.data.power);
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtils.showShort(responseObj.msg);
                        }
                    }
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onErrorResponse() {
                ToastUtils.showShort(R.string.login_failed);
                mProgressDialog.dismiss();
            }
        });
    }
}
