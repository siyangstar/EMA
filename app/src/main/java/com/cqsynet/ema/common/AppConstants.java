/*

 * Copyright (C) 2014 重庆尚渝
 * 版权所有
 *
 * 功能描述：常量文件
 *
 *
 * 创建标识：zhaosy 20140823
 */
package com.cqsynet.ema.common;

import com.cqsynet.ema.R;

import java.util.HashMap;
import java.util.Map;

public class AppConstants {

    // 开发环境地址
//    public static final String URL_MAIN = "http://kt_eam.frp.heikuai.net:5555/JKALL/a";//服务器路径
    //开发环境地址
    public static final String URL_MAIN = "http://113.204.4.244:8000/JKALL/a";//服务器路径

    public static final String URL_DICTIONARY = "/eam/phone/getDict"; //获取数据字典
    public static final String URL_LOGIN = "/login"; //登录
    public static final String URL_LOGOUT = "/logout"; //注销
    public static final String URL_GET_REPORT_LOCATION = "/eam/phone/getAllWz"; //获取所有报修位置
    public static final String URL_GET_SYSTEM_CATEGORY = "/eam/phone/getAllZl"; //获取所有系统分类
    public static final String URL_GET_ERROR_APPEARANCE = "/eam/phone/getGzXx"; //获取所有故障现象
    public static final String URL_GET_DEVICE = "/eam/phone/getSb"; //获取设备列表
    public static final String URL_SUBMIT_REPORT = "/eam/phone/baoxiu"; //新增报修
    public static final String URL_GET_WORK_ORDER = "/eam/phone/getGdList"; //获取工单列表
    public static final String URL_GET_ASSET = "/eam/phone/getZcList";// 获取资产列表
    public static final String URL_GET_USER_DEPARTMENT = "/sys/user/getUserOffice"; //获取用户部门
    public static final String URL_SAVE_USER_DEPARTMENT = "/sys/user/switchOffice"; //保存用户部门
    public static final String URL_UPLOAD_IMAGE = "/eam/phone/upload"; //上传图片
    public static final String URL_TODOLIST_NO = "/act/process/data"; //未办任务
    public static final String URL_TODOLIST_YES = "/act/process/hiUserTask"; //已办任务

    public static final String URL_WORKORDER_DETAIL = "http://113.204.4.244:8000/JKALL/static/phone/#/index"; //资产详情
    public static final String URL_ASSET_DETAIL = "http://113.204.4.244:8000/JKALL/static/phone/#/assets"; //工单详情

    public static final String TAG_ASSET = "asset";
    public static final String TAG_WORK_ORDER = "workOrder";
    public static final String TAG_KPI = "kpi";
    public static final String TAG_REPORT = "report";
    public static final String TAG_TODO_LIST = "todoList";
    public static final String TAG_DEVICE = "device";

    public static final String RET_OK = "0";

    public static final String ID_TODO_LIST = "3da97fde4f2443b180bc9c1e9237d766";
    public static final String ID_ASSET = "5328e8091922431cb9096cd5f314b91b";
    public static final String ID_WORKORDER = "0fea3ca379354ce6ab9f72ed1ff8fad8";
    public static final String ID_KPI = "62ad6e17ea714923973ac90e48018b23";
    public static final String ID_REPORT = "ddc85070f1ba418a8c086bab0c8568e8";
    public static final Map<String, Integer> ICON_MAP = new HashMap<String, Integer>() {
        {
            put(ID_ASSET, R.drawable.home_asset);
            put(ID_WORKORDER, R.drawable.home_workorder);
            put(ID_KPI, R.drawable.home_kpi);
            put(ID_REPORT, R.drawable.home_report);
            put(ID_TODO_LIST, R.drawable.home_todolist);
        }
    };
    //字典表类型
    public static final String DICTIONARY_TYPE_WORKORDER_PRIORITY = "gd_yxj"; //字典表类型:工单优先级
    public static final String DICTIONARY_TYPE_WORKORDER_STATUS = "gd_zt"; //字典表类型:工单状态

    public static final long UPDATE_DATE_INTEVAL = 24; //更新数据的间隔时间,单位:小时
}