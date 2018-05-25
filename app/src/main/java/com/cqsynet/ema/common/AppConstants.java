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

public class AppConstants {

    //开发环境地址
//    public static final String SERVER_URL = "http://cif-transferconfig.heikuai.com:8000/transferconfigif/interfaceCall";//转发服务器
//    public static final String SERVER_MULTIPART_URL = "http://172.16.19.58:8080/file/up";//文件服务器
//    public static final String SERVER_SOCKET_ADDRESS = "http://10.255.244.238:1994/heikuai"; //长连接socket地址

    //测试环境地址
//    public static final String SERVER_URL = "http://cif-transferconfig.heikuai.com:8000/transferconfigif/interfaceCall";//转发服务器
//    public static final String SERVER_MULTIPART_URL = "http://csrv-filerecsys.heikuai.com:8000/filerecsysif/interfaceCall.if";//文件服务器
//    public static final String SERVER_SOCKET_ADDRESS = "http://10.255.244.238:1994/heikuai"; //长连接socket地址

    // 预发布环境器地址
//    public static final String SERVER_URL = "http://yif-transferconfig.heikuai.com:9000/transferconfigif/interfaceCall";//转发服务器
//    public static final String SERVER_MULTIPART_URL = "http://ysrv-filerecsys.heikuai.com:9000/filerecsysif/interfaceCall.if";//文件服务器
//    public static final String SERVER_SOCKET_ADDRESS = "http://10.255.246.240:1994/heikuai"; //长连接socket地址
//    public static final String COMPLAIN_PAGE = "http://testweb.heikuai.com/complaint/index.html"; //投诉页面测试环境

    // 生产环境地址
    public static final String SERVER_URL = "http://sif-transferconfig.heikuai.com:8000/transferconfigif/interfaceCall";//转发服务器
    public static final String SERVER_MULTIPART_URL = "http://ssrv-filerecsys.heikuai.com:8000/filerecsysif/interfaceCall.if";//文件服务器
    public static final String SERVER_SOCKET_ADDRESS = "http://ssrv-nodejs.heikuai.com:1994/heikuai"; //长连接socket地址
    public static final String COMPLAIN_PAGE = "http://app.heikuai.com/complaint/index.html"; //投诉页面

    public static final String TAG_PATROL = "patrol";
    public static final String TAG_WORK_ORDER = "workOrder";
    public static final String TAG_KPI = "kpi";
    public static final String TAG_REPAIR = "report";
    public static final String TAG_TODO_LIST = "todoList";

}