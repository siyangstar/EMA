package com.cqsynet.ema.model;

import java.util.ArrayList;

/**
 * 获取故障现象接口返回类
 */
public class ErrorAppearanceResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public ArrayList<ErrorAppearanceObject> data;
    }
}
