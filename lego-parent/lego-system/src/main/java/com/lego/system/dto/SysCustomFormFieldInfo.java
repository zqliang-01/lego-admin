package com.lego.system.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SysCustomFormFieldInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private String appCode;
    private String tableName;
    private List<TypeInfo> columns;
    private List<List<SysCustomFieldInfo>> fields;

    public SysCustomFormFieldInfo(String appCode, String tableName, List<TypeInfo> columns, List<List<SysCustomFieldInfo>> fields) {
        this.appCode = appCode;
        this.tableName = tableName;
        this.columns = columns;
        this.fields = fields;
    }
}
