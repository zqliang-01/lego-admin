package com.lego.crm.action;

import com.lego.core.action.AddAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.crm.dao.ICrmContractDao;
import com.lego.crm.entity.CrmContract;
import com.lego.crm.vo.CrmContractCreateVO;
import com.lego.crm.entity.CrmCustomer;
import com.lego.crm.entity.CrmLead;

public class AddCrmContractAction extends AddAction<CrmContract, ICrmContractDao> {

    private CrmContractCreateVO vo;

    public AddCrmContractAction(String operatorCode, CrmContractCreateVO vo) {
        super("crm_contract", operatorCode);
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，合同新增失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，合同新增失败！");
    	BusinessException.check(vo.getStartTime() != null, "开始时间不能为空，合同新增失败！");
    	BusinessException.check(vo.getEndTime() != null, "结束时间不能为空，合同新增失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getOwner()), "负责人不能为空，合同新增失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getLead()), "线索不能为空，合同新增失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getCustomer()), "客户不能为空，合同新增失败！");
        BusinessException.check(!checkExists("code", vo.getCode()), "已存在编码为[{0}]的合同，合同新增失败！", vo.getCode());
    }

    @Override
    protected CrmContract createTargetEntity() {
        CrmContract entity = new CrmContract(vo.getCode(), vo.getName());
        entity.setStartTime(vo.getStartTime());
        entity.setEndTime(vo.getEndTime());
        entity.setOwner(vo.getOwner());
        entity.setAmount(vo.getAmount());
        entity.setType(vo.getType());
        entity.setAddress(vo.getAddress());
        entity.setLead(findByCode(CrmLead.class, vo.getLead()));
        entity.setCustomer(findByCode(CrmCustomer.class, vo.getCustomer()));
        return entity;
    }
}