package com.lego.system.service.impl;

import com.lego.core.data.hibernate.impl.BusService;
import com.lego.core.dto.LegoPage;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.CustomFieldTypeEnum;
import com.lego.core.vo.GenericConditionItemVO;
import com.lego.core.vo.GenericConditionVO;
import com.lego.core.vo.GenericSearchConditionEnum;
import com.lego.system.assembler.SysOperationLogAssembler;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.dao.ISysOperationLogDao;
import com.lego.system.dao.ISysPermissionDao;
import com.lego.system.dto.SysOperationLogInfo;
import com.lego.system.entity.SysOperationLog;
import com.lego.system.service.ISysOperationLogService;
import com.lego.system.vo.SysOperationLogSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysOperationLogService extends BusService<ISysOperationLogDao, SysOperationLogAssembler> implements ISysOperationLogService {

    @Autowired
    private ISysEmployeeDao employeeDao;

    @Autowired
    private ISysPermissionDao permissionDao;

    @Override
    public List<SysOperationLogInfo> findBy(String loginCode, String entityCode, String permissionCode) {
        List<SysOperationLog> logs = dao.findBy(loginCode, entityCode, permissionCode);
        return assembler.create(logs);
    }

    @Override
    public LegoPage<SysOperationLogInfo> findBy(SysOperationLogSearchVO vo) {
        GenericConditionVO condition = GenericConditionVO.create(vo);
        if (StringUtil.isNotBlank(vo.getDescription())) {
            condition.addItem(GenericConditionItemVO.createLike("description", "%" + vo.getDescription() + "%"));
        }
        if (StringUtil.isNotBlank(vo.getOperatorCode())) {
            condition.addItem(GenericConditionItemVO.createEqual(CustomFieldTypeEnum.ENTITY, "operator", vo.getOperatorCode()));
        }
        if (vo.getStartTime() != null) {
            condition.addItem(new GenericConditionItemVO(GenericSearchConditionEnum.GREATER_THEN_OR_EQUALS, CustomFieldTypeEnum.DATE, "createTime", vo.getStartTime()));
        }
        if (vo.getEndTime() != null) {
            condition.addItem(new GenericConditionItemVO(GenericSearchConditionEnum.LESS_THAN_OR_EQUALS, CustomFieldTypeEnum.DATE, "createTime", vo.getEndTime()));
        }
        return assembler.create(dao.findPageBy(condition));
    }

}
