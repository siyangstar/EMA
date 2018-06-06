package com.cqsynet.ema.model;

import java.util.ArrayList;

/**
 * 获取设备列表返回的对象
 */
public class DeviceListObject {
    public int pageNo;
    public int pageSize;
    public int count;
    public ArrayList<DeviceObject> list;
}
