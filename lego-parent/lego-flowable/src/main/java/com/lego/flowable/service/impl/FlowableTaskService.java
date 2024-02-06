package com.lego.flowable.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import com.lego.core.dto.LegoPage;
import com.lego.core.exception.BusinessException;
import com.lego.core.flowable.FlowableService;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.flowable.assembler.FlowableTaskAssembler;
import com.lego.flowable.dto.FlowableProcessNodeInfo;
import com.lego.flowable.dto.FlowableTaskFormDetailInfo;
import com.lego.flowable.dto.FlowableTaskInfo;
import com.lego.flowable.service.IFlowableTaskService;
import com.lego.flowable.vo.FlowableTaskCompleteVO;
import com.lego.flowable.vo.FlowableTaskSearchVO;
import com.lego.system.dao.ISysEmployeeDao;
import com.lego.system.entity.SysEmployee;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FlowableTaskService extends FlowableService<FlowableTaskAssembler> implements IFlowableTaskService {

    @Autowired
    private ISysEmployeeDao employeeDao;

    @Override
    public LegoPage<FlowableTaskInfo> findUndoBy(String employeeCode, FlowableTaskSearchVO vo) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        List<String> candidateGroups = EntityUtil.getCode(employee.getRoles());
        candidateGroups.add(EntityUtil.getCode(employee.getDept()));
        TaskQuery taskQuery = taskService.createTaskQuery()
            .active()
            .taskCandidateOrAssigned(employeeCode)
            .taskCandidateGroupIn(candidateGroups)
            .orderByTaskCreateTime().desc();
        if (StringUtil.isNotBlank(vo.getInstanceId())) {
            taskQuery.processInstanceId(vo.getInstanceId());
        }
        LegoPage<Task> page = createPage(taskQuery, vo, Task.class);
        return assembler.create(page);
    }

    @Override
    public LegoPage<FlowableTaskInfo> findCompletedBy(String employeeCode, FlowableTaskSearchVO vo) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        List<String> candidateGroups = EntityUtil.getCode(employee.getRoles());
        candidateGroups.add(EntityUtil.getCode(employee.getDept()));
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
            .finished()
            .or()
            .taskAssignee(employeeCode)
            .taskCandidateGroupIn(candidateGroups)
            .endOr()
            .orderByHistoricTaskInstanceEndTime()
            .desc();
        if (StringUtil.isNotBlank(vo.getInstanceId())) {
            taskInstanceQuery.processInstanceId(vo.getInstanceId());
        }
        LegoPage<HistoricTaskInstance> page = createPage(taskInstanceQuery, vo, HistoricTaskInstance.class);
        return assembler.createHisPage(page);
    }

    @Override
    public void complete(String employeeCode, FlowableTaskCompleteVO vo) {
        SysEmployee employee = employeeDao.findByCode(employeeCode);
        List<String> candidateGroups = EntityUtil.getCode(employee.getRoles());
        candidateGroups.add(EntityUtil.getCode(employee.getDept()));
        TaskQuery taskQuery = taskService.createTaskQuery()
            .taskId(vo.getId())
            .active()
            .taskCandidateOrAssigned(employeeCode)
            .taskCandidateGroupIn(candidateGroups)
            .orderByTaskCreateTime().desc();
        Task task = taskQuery.singleResult();

        BusinessException.check(task != null, "当前任务审核人非[{0}]，审核失败！", employeeCode);
        taskService.complete(vo.getId(), vo.getVariables());
    }

    /**
     * 查看当前流程图已到达节点(包含路径)
     */
    @Override
    public String getBpmnXml(String instanceId) {
        HistoricProcessInstance hisInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        String definitionId = hisInstance.getProcessDefinitionId();
        if (definitionId == "") {
            return null;
        }
        //获得活动的节点
        List<HistoricActivityInstance> historyProcess = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        List<String> taskTypes = Arrays.asList("userTask", "exclusiveGateway", "startEvent", "endEvent");
        for (HistoricActivityInstance hi : historyProcess) {
            String activityType = hi.getActivityType();
            if (activityType.equals("sequenceFlow")) {
                flows.add(hi.getActivityId());
            } else {
                activityIds.add(hi.getActivityId());
            }
        }
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(instanceId).list();
        for (Task runningTask : tasks) {
            activityIds.add(runningTask.getTaskDefinitionKey());
        }
        ProcessEngineConfiguration engConf = processEngine.getProcessEngineConfiguration();
        engConf.setActivityFontName("宋体");
        engConf.setLabelFontName("宋体");
        engConf.setAnnotationFontName("宋体");
        //定义流程画布生成器
        ProcessDiagramGenerator processDiagramGenerator = engConf.getProcessDiagramGenerator();
        InputStream in = processDiagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engConf.getActivityFontName()
            , engConf.getLabelFontName(), engConf.getAnnotationFontName(), engConf.getClassLoader(), 1.0, true);
        return StringUtil.encodeBase64(IoUtil.readBytes(in));
    }

    @Override
    public FlowableProcessNodeInfo findProcessNodeBy(String instanceId) {
        FlowableProcessNodeInfo nodeInfo = new FlowableProcessNodeInfo();
        // 构建查询条件
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(instanceId);
        List<HistoricActivityInstance> allActivityInstanceList = query.list();
        if (CollUtil.isEmpty(allActivityInstanceList)) {
            return nodeInfo;
        }
        // 查询所有已完成的元素
        List<HistoricActivityInstance> finishedElementList = allActivityInstanceList.stream()
            .filter(item -> item.getEndTime() != null).collect(Collectors.toList());
        // 所有已完成的连线
        Set<String> finishedSequenceFlowSet = new HashSet<>();
        // 所有已完成的任务节点
        Set<String> finishedTaskSet = new HashSet<>();
        finishedElementList.forEach(item -> {
            if (BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW.equals(item.getActivityType())) {
                nodeInfo.addFinishedFlow(item.getActivityId());
            } else {
                nodeInfo.addFinishedTask(item.getActivityId());
            }
        });
        // 查询所有未结束的节点
        Set<String> unfinishedTaskSet = allActivityInstanceList.stream()
            .filter(item -> item.getEndTime() != null)
            .map(HistoricActivityInstance::getActivityId)
            .collect(Collectors.toSet());
        nodeInfo.setUnfinishedTaskSet(unfinishedTaskSet);

        HistoricProcessInstance hisInstnace = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(instanceId)
            .singleResult();
        InputStream bpmnBytes = repositoryService.getProcessModel(hisInstnace.getProcessDefinitionId());
        nodeInfo.setXml(StringUtil.toString(bpmnBytes));
        return nodeInfo;
    }

    @Override
    public FlowableTaskFormDetailInfo findCodeVariable(String taskId) {
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery()
            .includeProcessVariables()
            .finished()
            .taskId(taskId)
            .singleResult();
        if (task != null) {
            String formKey = task.getFormKey();
            String code = (String) task.getProcessVariables().get("code");
            return new FlowableTaskFormDetailInfo(formKey, code);
        }
        return null;
    }

}
