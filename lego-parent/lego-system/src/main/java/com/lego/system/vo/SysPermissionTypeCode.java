package com.lego.system.vo;

import java.util.Arrays;
import java.util.List;

public interface SysPermissionTypeCode {

    String APP = "app";

    String MENU = "menu";

    String AUTH = "auth";

    String REPORT = "report";

    String[] SHOW_TYPES = new String[]{APP, MENU, REPORT};

    List<String> MENU_TYPES = Arrays.asList(MENU, REPORT);
}
