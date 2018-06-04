package com.cqsynet.ema.model;

/**
 * 获取工单列表接口返回类
 */
public class WorkOrderResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public WorkOrderListObject data;
    }
}
