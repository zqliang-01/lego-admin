package com.lego.crm.listener;

import cn.hutool.core.bean.BeanUtil;
import com.lego.core.common.Constants;
import com.lego.core.module.flowable.FlowableCheckStatus;
import com.lego.core.module.flowable.FlowableProcessConstants;
import com.lego.core.module.flowable.IFlowableCompletedListener;
import com.lego.core.util.StringUtil;
import com.lego.crm.service.ICrmContractService;
import com.lego.crm.vo.CrmContractCreateVO;
import com.lego.crm.vo.CrmContractModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class CrmContractCompletedListener implements IFlowableCompletedListener {

    @Autowired
    private ICrmContractService contractService;

    @Override
    public String taskCompleted(Map<String, Object> variables) {
        String code = StringUtil.toString(variables.get(FlowableProcessConstants.FORM_UNIQUE_KEY));
        if (StringUtil.isNotBlank(code) && contractService.exists(code)) {
            CrmContractModifyVO vo = BeanUtil.toBean(variables, CrmContractModifyVO.class);
            vo.setCheckDiff(false);
            contractService.update(Constants.loginCode.get(), vo);
            return code;
        }
        CrmContractCreateVO vo = BeanUtil.toBean(variables, CrmContractCreateVO.class);
        vo.setCheckStatus(FlowableCheckStatus.checking);
        return contractService.add(Constants.loginCode.get(), vo);
    }

    @Override
    public void taskRejected(String code) {
        contractService.updateCheckStatus(code, FlowableCheckStatus.rejected);
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