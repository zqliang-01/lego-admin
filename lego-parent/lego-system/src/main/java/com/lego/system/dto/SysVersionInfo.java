package com.lego.system.dto;

import com.lego.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysVersionInfo {

    private String version;
    private String newVersion;
    private boolean needUpdate;

    public SysVersionInfo(String version, String newVersion) {
        this.version = version;
        this.newVersion = newVersion;
        this.needUpdate = StringUtil.isNotBlank(version) && !StringUtil.equals(version, newVersion);
    }
}
