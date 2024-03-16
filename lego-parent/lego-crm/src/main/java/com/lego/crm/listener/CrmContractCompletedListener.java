package com.lego.crm.listener;

import cn.hutool.core.bean.BeanUtil;
import com.lego.core.common.Constants;
import com.lego.core.flowable.FlowableCheckStatus;
import com.lego.core.flowable.FlowableProcessConstants;
import com.lego.core.flowable.IFlowableTaskCompletedListener;
import com.lego.core.util.StringUtil;
import com.lego.crm.service.ICrmContractService;
import com.lego.crm.vo.CrmContractCreateVO;
import com.lego.crm.vo.CrmContractModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CrmContractCompletedListener implements IFlowableTaskCompletedListener {

    @Autowired
    private ICrmContractService contractService;

    @Override
    public void completed(boolean isSave, Map<String, Object> variables) {
        String code = StringUtil.toString(variables.get(FlowableProcessConstants.FORM_UNIQUE_KEY));
        if (contractService.exists(code)) {
            CrmContractModifyVO vo = BeanUtil.toBean(variables, CrmContractModifyVO.class);
            vo.setCheckDiff(isSave);
            contractService.update(Constants.loginCode.get(), vo);
            return;
        }
        CrmContractCreateVO vo = BeanUtil.toBean(variables, CrmContractCreateVO.class);
        contractService.add(Constants.loginCode.get(), vo);
    }

    @Override
    public void processCompleted(String code) {
        contractService.updateCheckStatus(code, FlowableCheckStatus.completed);
    }

    @Override
    public String getTableCode() {
        return "crm_contract";
    }
}
