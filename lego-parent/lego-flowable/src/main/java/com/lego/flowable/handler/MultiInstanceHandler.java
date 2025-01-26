package com.lego.flowable.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lego.core.module.flowable.FlowableProcessConstants;
import com.lego.system.mapper.SysEmployeeMapper;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 多实例处理类，目前在前端流程图代码写死
 */
@Component("multiInstanceHandler")
public class MultiInstanceHandler {

    @Autowired
    private SysEmployeeMapper employeeMapper;

    public Set<String> getUserIds(DelegateExecution execution) {
        Set<String> candidateUserIds = new LinkedHashSet<>();
        FlowElement flowElement = execution.getCurrentFlowElement();
        if (ObjectUtil.isNotEmpty(flowElement) && flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            String dataType = userTask.getAttributeValue(BpmnXMLConstants.FLOWABLE_EXTENSIONS_NAMESPACE, FlowableProcessConstants.CUSTOM_DATA_TYPE);
            if ("USERS".equals(dataType) && CollUtil.isNotEmpty(userTask.getCandidateUsers())) {
                candidateUserIds.addAll(userTask.getCandidateUsers());
            } else if (CollUtil.isNotEmpty(userTask.getCandidateGroups())) {
                List<String> groups = userTask.getCandidateGroups();
                if ("ROLES".equals(dataType)) {
                    candidateUserIds.addAll(employeeMapper.selectCodesByRoles(groups));
                } else if ("DEPTS".equals(dataType)) {
                    candidateUserIds.addAll(employeeMapper.selectCodesByDepts(groups));
                }
            }
        }
        return candidateUserIds;
    }
}
