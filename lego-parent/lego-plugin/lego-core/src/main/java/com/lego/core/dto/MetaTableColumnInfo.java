package com.lego.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetaTableColumnInfo extends DTO {

    private static final long serialVersionUID = 1L;

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列描述
     */
    private String columnComment;

    /**
     * 列类型
     */
    private String columnType;

    /**
     * 是否主键
     */
    private boolean isPk;

    /**
     * 是否自增
     */
    private boolean isIncrement;

    /**
     * 是否必填
     */
    private boolean isRequired;

    /**
     * 是否唯一
     */
    private boolean isUnique;

    /**
     * 排序
     */
    private Integer sort;
}
