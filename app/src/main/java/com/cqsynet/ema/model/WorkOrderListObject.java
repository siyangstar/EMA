package com.cqsynet.ema.model;

import java.util.ArrayList;

/**
 * 获取工单列表返回的对象
 */
public class WorkOrderListObject {
    public int pageNo;
    public int pageSize;
    public int count;
    public ArrayList<WorkOrderObject> list;
}
