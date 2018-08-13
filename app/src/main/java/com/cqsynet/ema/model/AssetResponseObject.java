package com.cqsynet.ema.model;

/**
 * 资产列表接口返回对象
 */
public class AssetResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public AssetListObject data;
    }
}
