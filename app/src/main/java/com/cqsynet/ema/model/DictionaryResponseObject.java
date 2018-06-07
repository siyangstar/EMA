package com.cqsynet.ema.model;

import java.util.ArrayList;

/**
 * 字典表接口返回对象
 */
public class DictionaryResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public ArrayList<DictionaryObject> data;
    }
}
