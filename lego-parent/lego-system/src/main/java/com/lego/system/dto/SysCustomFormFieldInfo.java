package com.lego.system.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SysCustomFormFieldInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private String appCode;
    private List<TypeInfo> columns;
    private List<List<SysCustomFieldInfo>> fields;

    public SysCustomFormFieldInfo(String appCode, List<List<SysCustomFieldInfo>> fields) {
        this.appCode = appCode;
        this.fields = fields;
    }

}
