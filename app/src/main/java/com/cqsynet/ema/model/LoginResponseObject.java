package com.cqsynet.ema.model;

import java.util.ArrayList;

/**
 * 登录接口返回对象
 */
public class LoginResponseObject extends ResponseObject {

    public DataObject data;

    public class DataObject {
        public String sessionid;
        public ArrayList<AuthorityObject> power;
        public ArrayList<AuthorityObject> allPower;
    }

}
