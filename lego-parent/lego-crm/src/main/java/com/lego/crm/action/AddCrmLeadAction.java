package com.lego.crm.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.crm.dao.ICrmLeadDao;
import com.lego.crm.entity.CrmLead;
import com.lego.crm.vo.CrmLeadCreateVO;
import com.lego.crm.entity.CrmCustomer;

public class AddCrmLeadAction extends AddAction<CrmLead, ICrmLeadDao> {

    private CrmLeadCreateVO vo;

    public AddCrmLeadAction(String operatorCode, CrmLeadCreateVO vo) {
        super("crm_lead", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，线索新增失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，线索新增失败！");
        BusinessException.check(!checkExists("code", vo.getCode()), "已存在编码为[{0}]的线索，线索新增失败！", vo.getCode());
    }

    @Override
    protected CrmLead createTargetEntity() {
        CrmLead entity = new CrmLead(vo.getCode(), vo.getName());
        entity.setMobile(vo.getMobile());
        entity.setAmount(vo.getAmount());
        entity.setBirthday(vo.getBirthday());
        entity.setAddress(vo.getAddress());
        entity.setAge(vo.getAge());
        entity.setSize(vo.getSize());
        entity.setStatus(vo.isStatus());
        entity.setEmail(vo.getEmail());
        entity.setEmployee(vo.getEmployee());
        entity.setDept(vo.getDept());
        entity.setSource(vo.getSource());
        entity.setCustomer(findByUnsureCode(CrmCustomer.class, vo.getCustomer()));
        return entity;
    }
}