package com.lego.crm.service.impl;

import com.lego.core.common.ServiceStartType;
import com.lego.core.data.ICommonService;
import com.lego.core.dto.TypeInfo;
import com.lego.core.feign.client.ISystemClient;
import com.lego.core.vo.ActionVO;
import com.lego.core.vo.SysMessageCreateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "lego.start-type", havingValue = ServiceStartType.microservice)
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
}
