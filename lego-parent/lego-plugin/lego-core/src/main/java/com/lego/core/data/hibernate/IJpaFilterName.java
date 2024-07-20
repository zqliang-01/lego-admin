package com.lego.core.data.hibernate;

public interface IJpaFilterName {

    /**
     * 基于creator_code过滤数据权限
     * 动态拼接条件 creator_code IN (:filterCodes)
     */
    String CREATOR_CODE = "creatorCodeFilter";

    /**
     * 基于dept_code过滤数据权限，需通用字段增加dept_code，暂未使用
     * 动态拼接条件 dept_code IN (:filterCodes)
     */
    String DEPT_CODE = "deptCodeFilter";
}
