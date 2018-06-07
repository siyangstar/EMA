package com.cqsynet.ema.model;

/**
 * 设备列表接口返回对象
 */
public class DeviceListResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public DeviceListObject data;
    }
}
