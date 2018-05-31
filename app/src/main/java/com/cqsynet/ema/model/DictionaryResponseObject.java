package com.cqsynet.ema.model;

import java.util.ArrayList;

/**
 * 获取字典表接口返回类
 */
public class DictionaryResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public ArrayList<DictionaryObject> data;
    }
}
