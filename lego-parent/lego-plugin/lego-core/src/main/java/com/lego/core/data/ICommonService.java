package com.lego.core.data;

import com.lego.core.dto.TypeInfo;
import com.lego.core.vo.ActionVO;
import com.lego.core.vo.SysMessageCreateVO;

import java.util.List;

/**
 * 跨服务接口，组织架构类查询建议使用Redis缓存
 */
public interface ICommonService {

    void addLog(ActionVO vo);

    TypeInfo findEmployeeBy(String code);

    TypeInfo findDeptBy(String code);

    void addSysMessage(SysMessageCreateVO vo);

    String findReportCodeBy(String permissionCode);

    List<String> findPermissionCodesBy(String employeeCode);

    List<String> findRoleCodesBy(String employeeCode);

    List<String> findDataPermissionEmployeeCode();
}