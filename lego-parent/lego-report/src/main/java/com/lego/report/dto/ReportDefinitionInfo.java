package com.lego.report.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import com.lego.core.common.BooleanConverter;
import com.lego.core.common.TypeInfoConverter;
import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReportDefinitionInfo extends DTO {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "编码")
    private String code;
    @ExcelProperty(value = "姓名")
    private String name;
    @ExcelProperty(value = "数据源")
    private String dataSource;
    @ExcelProperty(value = "序列")
    private int sn;
    @ExcelProperty(value = "是否生效", converter = BooleanConverter.class)
    private boolean enable;
    @ExcelProperty(value = "SQL脚本")
    private String sqlText;
    @ExcelProperty(value = "最大导出数量")
    private int maxExportSize;
    @ExcelProperty(value = "负责人", converter = TypeInfoConverter.class)
    private TypeInfo creator;
    private String type;

    private List<ReportTitleInfo> titles = new ArrayList<>();
    private List<ReportConditionInfo> params = new ArrayList<>();
}
