package com.cqsynet.ema.model;

import java.util.ArrayList;

/**
 * 登录接口返回类
 */
public class LoginResponseObject {

    public String message;
    public String ret;
    public String sessionid;
    public ArrayList<AuthorityObject> power;
}
