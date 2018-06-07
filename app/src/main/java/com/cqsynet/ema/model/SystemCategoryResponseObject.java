package com.cqsynet.ema.model;

import java.util.ArrayList;

/**
 * 系统分类接口返回对象
 */
public class SystemCategoryResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public ArrayList<SystemCategoryObject> data;
    }
}
