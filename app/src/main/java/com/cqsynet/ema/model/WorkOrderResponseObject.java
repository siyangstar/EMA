package com.cqsynet.ema.model;

/**
 * 工单列表接口返回对象
 */
public class WorkOrderResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public WorkOrderListObject data;
    }
}
