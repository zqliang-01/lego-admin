package com.lego.system.vo;

import java.util.Arrays;
import java.util.List;

public interface SysPermissionCode {

    String manage = "manage";
    String manageUser = "manage_users";
    String manageRole = "manage_role";
    String manageCustomForm = "manage_customForm";
    String managePermission = "manage_permission";
    String manageGenTable = "manage_genTable";
    String manageWorkFlow = "manage_workflow";
    String manageWorkFlowModel = "manage_workflow_model";
    String oaUndo = "oa_undo";

    List<String> SYSTEM_APP = Arrays.asList("manage");
}
