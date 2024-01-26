package com.lego.system.vo;

import java.util.Arrays;
import java.util.List;

public interface SysPermissionCode {

    String manage = "manage";
    String manageUser = "manage_users";
    String manageRole = "manage_role";
    String manageCustomForm = "manage_customForm";
    String manageGenTable = "manage_genTable";
    String manageWorkFlow = "manage_workflow";

    List<String> SYSTEM_APP = Arrays.asList("manage");
}
