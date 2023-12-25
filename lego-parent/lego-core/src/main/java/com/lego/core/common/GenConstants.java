package com.lego.core.common;

import java.util.Arrays;
import java.util.List;

public interface GenConstants {

	String ROOT_PACKAGE_NAME = "com.lego";

	/**
     * 数据库字符串类型
     */
    List<String> COLUMNTYPE_STRING = Arrays.asList("char", "varchar", "nvarchar", "varchar2");

    /**
     * 数据库文本类型
     */
    List<String> COLUMNTYPE_TEXT = Arrays.asList("tinytext", "text", "mediumtext", "longtext");

    /**
     * 数据库时间类型
     */
    List<String> COLUMNTYPE_TIME = Arrays.asList("datetime", "time", "date", "timestamp");

    /**
     * 数据库整型数字类型
     */
    List<String> COLUMNTYPE_NUMBER = Arrays.asList("tinyint", "smallint", "mediumint", "int", "number", "integer", "bit", "bigint");

    /**
     * 数据库浮点型数字类型
     */
    List<String> COLUMNTYPE_FLOAT = Arrays.asList("float", "double", "decimal");

    /**
     * 关联表字段类型
     */
    List<String> COLUMNTYPE_REFERENCE_ENTITY = Arrays.asList("bigint");

    /**
     * 忽略创建的字段名
     */
    List<String> COLUMNNAME_IGNORE_GEN = Arrays.asList("id", "version", "create_time", "creator_code", "update_time");

    /**
     * 实体忽略的字段名
     */
    List<String> COLUMNNAME_IGNORE_ENTITY = Arrays.asList("id", "code", "name", "version", "create_time", "creator_code", "update_time");

    List<String> NULL = Arrays.asList();
}
