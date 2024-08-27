package com.lego.crm.action;

import com.lego.core.action.ModifyAction;
import com.lego.core.exception.BusinessException;
import com.lego.core.util.StringUtil;
import com.lego.crm.dao.ICrmContractDao;
import com.lego.crm.entity.CrmContract;
import com.lego.crm.vo.CrmContractModifyVO;
import com.lego.crm.entity.CrmDictionary;
import com.lego.crm.entity.CrmCustomer;
import com.lego.crm.entity.CrmLead;

public class ModifyCrmContractAction extends ModifyAction<CrmContract, ICrmContractDao> {

    private CrmContractModifyVO vo;

    public ModifyCrmContractAction(String operatorCode, CrmContractModifyVO vo) {
        super("crm_contract", operatorCode, vo.getCode());
        this.setCheckDiff(vo.isCheckDiff());
        this.vo = vo;
    }

    @Override
    protected void preprocess() {
        BusinessException.check(StringUtil.isNotBlank(vo.getCode()), "编码不能为空，合同修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getName()), "名称不能为空，合同修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getLead()), "线索不能为空，合同修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getCustomer()), "客户不能为空，合同修改失败！");
        BusinessException.check(StringUtil.isNotBlank(vo.getType()), "类型不能为空，合同修改失败！");
    }

    @Override
    protected void doModify(CrmContract entity) {
        entity.setName(vo.getName());
        entity.setStartTime(vo.getStartTime());
        entity.setEndTime(vo.getEndTime());
        entity.setOwnerCode(vo.getOwnerCode());
        entity.setAmount(vo.getAmount());
        entity.setLead(findByCode(CrmLead.class, vo.getLead()));
        entity.setCustomer(findByCode(CrmCustomer.class, vo.getCustomer()));
        entity.setType(findByCode(CrmDictionary.class, vo.getType()));
    }

}
