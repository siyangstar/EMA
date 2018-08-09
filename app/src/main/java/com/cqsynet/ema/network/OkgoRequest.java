/*
 * Copyright (C) 2014 重庆尚渝
 * 版权所有
 *
 * 功能描述：使用OkGo发起http请求
 *
 *
 * 创建标识：zhaosy 20140823
 */
package com.cqsynet.ema.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cqsynet.ema.activity.LoginActivity;
import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.common.Globals;
import com.cqsynet.ema.model.ResponseObject;
import com.cqsynet.ema.util.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONException;

import java.io.File;
import java.util.Map;

/**
 * http网络请求类
 */
public class OkgoRequest {

    public static final String TAG = "EMARequest";

    /**
     * 处理不含文件的请求
     *
     * @param context
     * @param url
     * @param paramMap
     * @param callbackIf
     */
    public static void excute(final Context context, String url, Map<String, String> paramMap, final IResponseCallback callbackIf) {
        excute(context, url, paramMap, null, callbackIf);
    }

    /**
     * 处理请求
     *
     * @param url
     */
    public static void excute(final Context context, String url, Map<String, String> paramMap, File file, final IResponseCallback callbackIf) {
        final String requestUrl = url;
        if (Globals.DEBUG) {
            Log.d(TAG, "请求:  " + url);
            if (paramMap != null) {
                Gson gson = new Gson();
                String requestStr = gson.toJson(paramMap).toString();
                Log.d(TAG, requestStr.trim());
            } else {
                Log.d(TAG, "无特殊参数");
            }
        }
        if (url.equals(AppConstants.URL_LOGIN)) {
            url = AppConstants.URL_MAIN + url;
        } else {
            url = AppConstants.URL_MAIN + url + ";JSESSIONID=" + SharedPreferencesUtil.getTagString(context, SharedPreferencesUtil.SEESION_ID);
        }
        PostRequest post = OkGo.<String>post(url);
        if (file == null) {
            post.isMultipart(false);
        } else {
            post.isMultipart(true);
            post.params("file", file);
        }
        post.params(paramMap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String msg = response.body();
                            if (Globals.DEBUG) {
                                Log.d(TAG, "响应:  " + requestUrl);
                                msg = msg.trim();
                                int index = 0;
                                int maxLength = 4000;
                                String sub;
                                while (index < msg.length()) {
                                    // java的字符不允许指定超过总的长度end
                                    if (msg.length() <= index + maxLength) {
                                        sub = msg.substring(index);
                                    } else {
                                        sub = msg.substring(index, index + maxLength);
                                    }

                                    index += maxLength;
                                    Log.d(TAG, sub.trim());
                                }
                            }
                            // 调用UI端的回调函数
                            Gson gson = new Gson();
                            ResponseObject object = gson.fromJson(msg, ResponseObject.class);
                            if (object.ret.equals("1") && object.msg.equals("session过期")) {

                            }
                            callbackIf.onResponse(msg);
                        } catch (JsonSyntaxException e) {
                            //注销当前用户,跳转到登录界面
                            SharedPreferencesUtil.removeData(context, SharedPreferencesUtil.SEESION_ID);
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        } catch (Exception e) {
                            callbackIf.onErrorResponse();
                            if (Globals.DEBUG) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        // 调用UI端注册的错误处理回调
                        callbackIf.onErrorResponse();
                        if (Globals.DEBUG) {
                            response.getException().printStackTrace();
                        }
                    }
                });
    }


    /*
     * 回调函数接口
     */
    public interface IResponseCallback {

        void onResponse(String response) throws JSONException; // 正常返回时的回调

        void onErrorResponse(); // 错误处理的回调
    }
}
