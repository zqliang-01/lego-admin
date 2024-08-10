package com.lego.system.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysSystemInfo extends DTO {

    private static final long serialVersionUID = 1L;

    private String companyName;
    private String companyLogo;
    private String version;
}
