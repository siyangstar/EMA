package com.cqsynet.ema.model;

/**
 * 获取设备列表接口返回类
 */
public class DeviceListResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public DeviceListObject data;
    }
}
