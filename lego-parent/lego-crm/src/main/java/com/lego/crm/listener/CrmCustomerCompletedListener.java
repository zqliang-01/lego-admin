package com.lego.crm.listener;

import cn.hutool.core.bean.BeanUtil;
import com.lego.core.common.Constants;
import com.lego.core.flowable.FlowableCheckStatus;
import com.lego.core.flowable.FlowableProcessConstants;
import com.lego.core.flowable.IFlowableTaskCompletedListener;
import com.lego.core.util.StringUtil;
import com.lego.crm.service.ICrmCustomerService;
import com.lego.crm.vo.CrmCustomerCreateVO;
import com.lego.crm.vo.CrmCustomerModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CrmCustomerCompletedListener implements IFlowableTaskCompletedListener {

    @Autowired
    private ICrmCustomerService customerService;

    @Override
    public void completed(boolean isSave, Map<String, Object> variables) {
        String code = StringUtil.toString(variables.get(FlowableProcessConstants.FORM_UNIQUE_KEY));
        if (customerService.exists(code)) {
            CrmCustomerModifyVO vo = BeanUtil.toBean(variables, CrmCustomerModifyVO.class);
            vo.setCheckDiff(isSave);
            customerService.update(Constants.loginCode.get(), vo);
            return;
        }
        CrmCustomerCreateVO vo = BeanUtil.toBean(variables, CrmCustomerCreateVO.class);
        customerService.add(Constants.loginCode.get(), vo);
    }

    @Override
    public void processCompleted(String code) {
        customerService.updateCheckStatus(code, FlowableCheckStatus.completed);
    }

    @Override
    public String getTableCode() {
        return "crm_customer";
    }
}
