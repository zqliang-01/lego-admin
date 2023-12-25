package com.lego.core.dto;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.lego.core.common.TypeInfoConverter;
import com.lego.core.util.DateUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusiDTO extends VersionDTO {

	private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "创建时间")
    @DateTimeFormat(DateUtil.dateTimePattern)
    private Date createTime;

    @ExcelProperty(value = "更新时间")
    @DateTimeFormat(DateUtil.dateTimePattern)
    private Date updateTime;

    @ExcelProperty(value = "创建人", converter = TypeInfoConverter.class)
    private TypeInfo creator;

}
