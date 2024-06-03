package com.lego.system.dto;

import com.lego.core.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SysCustomFormListInfo extends DTO {

    private SysCustomFormInfo form;
    private List<SysCustomFieldInfo> fields;
}
