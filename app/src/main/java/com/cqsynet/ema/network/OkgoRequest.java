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

import android.util.Log;

import com.cqsynet.ema.common.AppConstants;
import com.cqsynet.ema.common.Globals;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;

import java.util.Map;

public class OkgoRequest {

    public static final String TAG = "EMARequest";

    /**
     * 处理请求
     *
     * @param url
     */
    public static void excute(String url, Map<String, String> paramMap, final IResponseCallback callbackIf) {
        if(Globals.DEBUG) {
            Gson gson = new Gson();
            String requestStr = gson.toJson(paramMap).toString();
            Log.d(TAG, "请求:  ");
            Log.d(TAG, requestStr.trim());
        }
        OkGo.<String>post(AppConstants.URL_MAIN + url)
                .params(paramMap)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String msg = response.body();
                            if (Globals.DEBUG) {
                                Log.d(TAG, "响应:  ");
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
                            callbackIf.onResponse(msg);
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