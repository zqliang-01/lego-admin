package com.lego.system.vo;

import java.util.Arrays;
import java.util.List;

public interface SysPermissionCode {

    String manage = "manage";
	String manageUser = "manage:users";
	String manageRole = "manage:role";
	String manageCustomForm = "manage:customForm";
	String manageGenTable = "manage:genTable";

	List<String> SYSTEM_APP = Arrays.asList("manage");
}
