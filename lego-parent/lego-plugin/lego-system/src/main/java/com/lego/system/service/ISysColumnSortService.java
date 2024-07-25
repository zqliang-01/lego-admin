package com.lego.system.service;

import com.lego.system.dto.SysColumnSortInfo;
import com.lego.system.vo.SysColumnSortModifyVO;
import com.lego.system.vo.SysColumnWidthModifyVO;

import java.util.List;

public interface ISysColumnSortService {

    List<SysColumnSortInfo> findAndInitBy(String formCode, String employeeCode);

    void update(String employeeCode, List<SysColumnSortModifyVO> vos);

    void updateWidth(String loginCode, SysColumnWidthModifyVO vo);
}
