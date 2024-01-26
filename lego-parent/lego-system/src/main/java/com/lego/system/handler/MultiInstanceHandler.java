package com.lego.system.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lego.core.common.ProcessConstants;
import com.lego.system.mapper.SysEmployeeMapper;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 多实例处理类，目前入口在前端代码写死
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
            String dataType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_CUSTOM_DATA_TYPE);
            if ("USERS".equals(dataType) && CollUtil.isNotEmpty(userTask.getCandidateUsers())) {
                candidateUserIds.addAll(userTask.getCandidateUsers());
            } else if (CollUtil.isNotEmpty(userTask.getCandidateGroups())) {
                List<String> groups = userTask.getCandidateGroups()
                    .stream().map(item -> item.substring(4)).collect(Collectors.toList());
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
