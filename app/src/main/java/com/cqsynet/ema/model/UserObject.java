package com.cqsynet.ema.model;

import java.util.ArrayList;

/**
 * 用户部门对象
 */
public class UserObject {
    public String name; //姓名
    public String no; //工号
    public DepartmentObject office; //当前部门
    public ArrayList<DepartmentObject> officeList; //部门列表
}
