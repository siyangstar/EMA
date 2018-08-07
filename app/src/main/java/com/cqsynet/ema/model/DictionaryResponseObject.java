package com.cqsynet.ema.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 字典表接口返回对象
 */
public class DictionaryResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public HashMap<String, HashMap<String, String>> process;
        public HashMap<String, ArrayList<DictionaryObject>> dict;
    }
}
