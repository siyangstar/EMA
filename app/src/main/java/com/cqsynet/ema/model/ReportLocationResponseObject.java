package com.cqsynet.ema.model;

import java.util.ArrayList;

/**
 * 获取报修位置接口返回类
 */
public class ReportLocationResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public ArrayList<LocationObject> data;
    }
}
