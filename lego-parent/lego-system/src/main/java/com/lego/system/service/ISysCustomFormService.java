package com.lego.system.service;

import com.lego.core.dto.LegoPage;
import com.lego.core.dto.TypeInfo;
import com.lego.system.dto.SysCustomFormInfo;
import com.lego.system.dto.SysCustomFormPermissionInfo;
import com.lego.system.vo.SysCustomFormCreateVO;
import com.lego.system.vo.SysCustomFormFieldModifyVO;
import com.lego.system.vo.SysCustomFormModifyVO;
import com.lego.system.vo.SysCustomFormSearchVO;

import java.util.List;

public interface ISysCustomFormService {

    LegoPage<SysCustomFormInfo> findBy(SysCustomFormSearchVO vo);

    SysCustomFormInfo findBy(String code);

    SysCustomFormPermissionInfo findPermissionBy(String code);

    void modifyField(String operatorCode, SysCustomFormFieldModifyVO vo);

    void modify(String operatorCode, SysCustomFormModifyVO vo);

    void add(String employeeCode, SysCustomFormCreateVO vo);

    void delete(String operatorCode, String code);


    SysCustomFormInfo findInitByTable(String tableCode);

    List<TypeInfo> findSimpleType();

    String findTableCodeBy(String code);
}
