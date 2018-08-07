package com.cqsynet.ema.model;

/**
 * 用户部门接口返回对象
 */
public class UserResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public UserObject data;
    }
}
