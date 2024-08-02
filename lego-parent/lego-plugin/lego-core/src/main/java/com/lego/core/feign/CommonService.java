package com.lego.core.feign;

import com.lego.core.data.ICommonService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.feign.client.ISystemClient;
import com.lego.core.vo.ActionVO;
import com.lego.core.vo.SysMessageCreateVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CommonService implements ICommonService {

    @Autowired
    private ISystemClient systemClient;

    @Override
    public void addLog(ActionVO vo) {
        systemClient.addLog(vo);
    }

    @Override
    public TypeInfo findEmployeeBy(String code) {
        return systemClient.findEmployeeBy(code).getData();
    }

    @Override
    public TypeInfo findDeptBy(String code) {
        return systemClient.findDeptBy(code).getData();
    }

    @Override
    public void addSysMessage(SysMessageCreateVO vo) {
        systemClient.addSysMessage(vo);
    }

    @Override
    public String findReportCodeBy(String permissionCode) {
        return systemClient.findReportCodeBy(permissionCode).getData();
    }

    @Override
    public List<String> findPermissionCodesBy(String employeeCode) {
        return systemClient.findPermissionCodesBy(employeeCode).getData();
    }

    @Override
    public List<String> findRoleCodesBy(String employeeCode) {
        return systemClient.findRoleCodesBy(employeeCode).getData();
    }

    @Override
    public List<String> findDataPermissionEmployeeCode() {
        return systemClient.findDataPermissionEmployeeCode().getData();
    }

}
