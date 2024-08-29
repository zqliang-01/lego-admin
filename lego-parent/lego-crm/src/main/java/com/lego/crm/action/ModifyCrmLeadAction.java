package com.lego.crm.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.crm.dao.ICrmLeadDao;
import com.lego.crm.entity.CrmLead;
import com.lego.crm.vo.CrmLeadModifyVO;
import com.lego.crm.entity.CrmDictionary;
import com.lego.crm.entity.CrmCustomer;

public class ModifyCrmLeadAction extends ModifyAction<CrmLead, ICrmLeadDao> {

    private CrmLeadModifyVO vo;

    public ModifyCrmLeadAction(String operatorCode, CrmLeadModifyVO vo) {
        super("crm_lead", operatorCode, vo.getCode());
        this.setCheckDiff(vo.isCheckDiff());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，线索修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getEmployee()), "员工不能为空，线索修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getDept()), "部门不能为空，线索修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getSource()), "来源不能为空，线索修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getCustomer()), "客户不能为空，线索修改失败！");
    }

    @Override
    protected void doModify(CrmLead entity) {
        entity.setName(vo.getName());
        entity.setMobile(vo.getMobile());
        entity.setAmount(vo.getAmount());
        entity.setBrithday(vo.getBrithday());
        entity.setAddress(vo.getAddress());
        entity.setAge(vo.getAge());
        entity.setSize(vo.getSize());
        entity.setStatus(vo.isStatus());
        entity.setEmail(vo.getEmail());
        entity.setEmployee(vo.getEmployee());
        entity.setDept(vo.getDept());
        entity.setSource(findByCode(CrmDictionary.class, vo.getSource()));
        entity.setCustomer(findByCode(CrmCustomer.class, vo.getCustomer()));
    }
}