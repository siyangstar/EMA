/*
 * Copyright (C) 2014 重庆尚渝
 * 版权所有
 *
 * 功能描述：sharinfo工具类。
 *
 *
 * 创建标识：luchaowei 20141020
 */
package com.cqsynet.ema.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

public class SharedPreferencesUtil {
    public static final String EMA_PREFERENCES = "ema_data"; // shareinfo 名称
    public static final String SEESION_ID = "sessionId"; //会话session
    public static final String VERSION = "version"; // 上次退出时的版本号,用于判读是否新升级后的第一次打开app
    public static final String UPDATE_DICTIONARY_DATE = "update_dict_date"; //字典数据更新时间
    public static final String UPDATE_LOCATION_DATE = "update_location_date"; //位置数据更新时间
    public static final String UPDATE_SYSTEM_CATEGORY_DATE = "update_system_category_date"; //系统分类数据更新时间
    public static final String UPDATE_ERROR_APPEARANCE_DATE = "update_error_appearance_date"; //故障现象数据更新时间


    /**
     * @param context
     * @param key     存储的名称
     * @param values  要存储的字符串
     * @Description: 保存String类型的字符串到shareinfo。
     * @return: void
     */
    public static void setTagString(Context context, String key, String values) {
        SharedPreferences preferences = context.getSharedPreferences(EMA_PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(key, values);
        editor.commit();
    }

    /**
     * @param context
     * @param key     要获取的key名称。
     * @return
     * @Description: 从shareinfo里面获取字符串。
     * @return: String 得到的字符串。获取失败返回null。
     */
    public static String getTagString(Context context, String key) {
        return context.getSharedPreferences(EMA_PREFERENCES, Context.MODE_PRIVATE).getString(key, "");
    }

    /**
     * @param context
     * @param key     存储的名称
     * @param values  要存储long值
     * @Description: 保存long类型到shareinfo。
     * @return: void
     */
    public static void setTagLong(Context context, String key, long values) {
        SharedPreferences preferences = context.getSharedPreferences(EMA_PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putLong(key, values);
        editor.commit();
    }

    /**
     * @param context
     * @param key     要获取的key
     * @return
     * @Description: 获取long数据
     * @return: long 返回获取到的整形值。获取失败返回0.
     */
    public static long getTagLong(Context context, String key) {
        return context.getSharedPreferences(EMA_PREFERENCES, Context.MODE_PRIVATE).getLong(key, 0L);
    }

    /**
     * @param context
     * @param key     要保存的key。
     * @param values  要保存的整形值
     * @Description: 保存整形数据到shareinfo
     * @return: void
     */
    public static void setTagInt(Context context, String key, int values) {
        SharedPreferences preferences = context.getSharedPreferences(EMA_PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putInt(key, values);
        editor.commit();
    }

    /**
     * @param context
     * @param key     要获取的key
     * @return
     * @Description: 获取整形数据
     * @return: int 返回获取到的整形值。获取失败返回0.
     */
    public static int getTagInt(Context context, String key) {
        return context.getSharedPreferences(EMA_PREFERENCES, Context.MODE_PRIVATE).getInt(key, 0);
    }

    /**
     * @param context
     * @param key          要获取的key
     * @param defaultValue 默认值
     * @Description: 获取整形数据
     * @return: int 返回获取到的整形值。获取失败返回0.
     */
    public static int getTagInt(Context context, String key, int defaultValue) {
        return context.getSharedPreferences(EMA_PREFERENCES, Context.MODE_PRIVATE).getInt(key, defaultValue);
    }

    /**
     * @param context
     * @param key     要保存的key。
     * @param values  要保存的布尔值。
     * @Description: 保存布尔类型的数据到shareinfo
     * @return: void
     */
    public static void setTagBoolean(Context context, String key, boolean values) {
        SharedPreferences preferences = context.getSharedPreferences(EMA_PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(key, values);
        editor.commit();
    }

    /**
     * @param context
     * @param key     要获取的key。
     * @return
     * @Description: 从shareinfo获取布尔值。
     * @return: boolean 获取到的布尔值。获取失败返回false。
     */
    public static boolean getTagBoolean(Context context, String key, boolean defaultValue) {
        return context.getSharedPreferences(EMA_PREFERENCES, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    /**
     * @param context
     * @param key     要保存的key
     * @param values  要保存的值
     * @return
     * @Description: 保存set<String>集合类型数据
     * @return: set<String> 返回获取到的set集合。获取失败返回null.
     */
    public static void setTagSet(Context context, String key, Set<String> values) {
        SharedPreferences preferences = context.getSharedPreferences(EMA_PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putStringSet(key, values);
        editor.commit();
    }

    /**
     * @param context
     * @param key     要获取的key
     * @return
     * @Description: 获取set<String>集合类型数据
     * @return: set<String> 返回获取到的set集合。获取失败返回null.
     */
    public static Set<String> getTagSet(Context context, String key) {
        return context.getSharedPreferences(EMA_PREFERENCES, Context.MODE_PRIVATE).getStringSet(key, null);
    }

    /**
     * @param context
     * @param key     要删除的key
     * @return
     * @Description: 删除对应Key的数据
     * @return: void
     */
    public static void removeData(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(EMA_PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
