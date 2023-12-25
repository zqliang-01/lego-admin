package com.lego.sharding.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import com.lego.core.common.BooleanConverter;
import com.lego.core.common.TypeInfoConverter;
import com.lego.core.dto.TypeInfo;
import com.lego.core.dto.BusiDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingTableInfo extends BusiDTO {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "编码")
    private String code;
    @ExcelProperty(value = "名称")
    private String name;
    @ExcelProperty(value = "生效", converter = BooleanConverter.class)
    private boolean enable;
    @ExcelProperty(value = "描述")
    private String description;
    @ExcelProperty(value = "逻辑表名称")
    private String logicTableName;
    @ExcelProperty(value = "物理表表达式")
    private String actualDataNodes;
    @ExcelProperty(value = "本片字段")
    private String shardingColumn;
    @ExcelProperty(value = "算法", converter = TypeInfoConverter.class)
    private TypeInfo algorithm;
    @ExcelProperty(value = "模板", converter = TypeInfoConverter.class)
    private TypeInfo template;
    @ExcelProperty(value = "规则", converter = TypeInfoConverter.class)
    private TypeInfo config;
}
