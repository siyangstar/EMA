package com.cqsynet.ema.model;

/**
 * 待办任务列表接口返回对象
 */
public class TodoListResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public TodoListObject data;
    }
}
