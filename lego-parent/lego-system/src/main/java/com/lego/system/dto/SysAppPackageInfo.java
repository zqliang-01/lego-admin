package com.lego.system.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SysAppPackageInfo extends DTO {

    private String code;
    private String name;
    private String description;
    private String version;
    private Map<String, String> path = new HashMap<>();
}
