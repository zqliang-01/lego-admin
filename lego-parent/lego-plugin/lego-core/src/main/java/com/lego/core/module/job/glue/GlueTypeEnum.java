package com.lego.core.module.job.glue;

import com.lego.core.dto.TypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuxueli on 17/4/26.
 */
public enum GlueTypeEnum {

    BEAN("BEAN", false, null, null, null),
    GLUE_GROOVY("GLUE(Java)", false, null, null, "java.vm"),
    GLUE_SHELL("GLUE(Shell)", true, "bash", ".sh", "shell.vm"),
    GLUE_PYTHON("GLUE(Python)", true, "python", ".py", "python.vm"),
    GLUE_PHP("GLUE(PHP)", true, "php", ".php", "php.vm"),
    GLUE_NODEJS("GLUE(Nodejs)", true, "node", ".js", "nodejs.vm"),
    GLUE_POWERSHELL("GLUE(PowerShell)", true, "powershell", ".ps1", "powershell.vm");

    private String desc;
    private boolean isScript;
    private String cmd;
    private String suffix;
    private String templateName;

    private GlueTypeEnum(String desc, boolean isScript, String cmd, String suffix, String templateName) {
        this.desc = desc;
        this.isScript = isScript;
        this.cmd = cmd;
        this.suffix = suffix;
        this.templateName = templateName;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isScript() {
        return isScript;
    }

    public String getCmd() {
        return cmd;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getTemplateName() {
        return templateName;
    }

    public static GlueTypeEnum match(String name) {
        for (GlueTypeEnum item : GlueTypeEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public static List<TypeInfo> getTypeInfo() {
        List<TypeInfo> infos = new ArrayList<>();
        for (GlueTypeEnum item : values()) {
            infos.add(new TypeInfo(item.name(), item.desc));
        }
        return infos;
    }
}
