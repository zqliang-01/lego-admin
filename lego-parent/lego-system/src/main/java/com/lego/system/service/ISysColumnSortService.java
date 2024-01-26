package com.lego.system.service;

import com.lego.system.dto.SysColumnSortInfo;
import com.lego.system.vo.SysColumnSortModifyVO;

import java.util.List;

public interface ISysColumnSortService {

    List<SysColumnSortInfo> findBy(String formCode, String employeeCode);

    void updateBy(String formCode, String employeeCode, List<String> fieldCodes);

    void update(String employeeCode, List<SysColumnSortModifyVO> vos);

    void update(String employeeCode, SysColumnSortModifyVO vo);
}
