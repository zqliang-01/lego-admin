package com.lego.flowable.action;

import com.lego.core.action.MaintainAction;
import com.lego.core.data.ActionType;
import com.lego.core.data.hibernate.ICommonService;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.SysMessageCreateVO;
import com.lego.core.vo.SysMessageTypeEnum;
import com.lego.core.web.LegoBeanFactory;
import com.lego.system.vo.SysPermissionCode;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;

import java.util.List;

public class SendSysMessageAction extends MaintainAction {

    private String instanceId;

    private TaskService taskService = LegoBeanFactory.getBean(TaskService.class);
    private RuntimeService runtimeService = LegoBeanFactory.getBean(RuntimeService.class);

    public SendSysMessageAction(String operatorCode, String instanceId) {
        super(SysPermissionCode.manageWorkFlow, operatorCode);
        this.instanceId = instanceId;
    }

    @Override
    protected void doRun() {
        List<Task> list = taskService.createTaskQuery()
            .processInstanceId(instanceId)
            .list();
        ICommonService commonService = LegoBeanFactory.getBeanWithNull(ICommonService.class);
        list.stream().forEach(task -> {
            String assignee = task.getAssignee();
            if (StringUtil.isNotBlank(assignee)) {
                SysMessageCreateVO messageVO = new SysMessageCreateVO();
                messageVO.setRecipient(assignee);
                messageVO.setCreator(operatorCode);
                messageVO.setName(task.getName());
                messageVO.setEntityCode(task.getId());
                messageVO.setFormCode(task.getFormKey());
                messageVO.setType(SysMessageTypeEnum.FLOWABLE.getCode());
                messageVO.setContent("${creator} 发起了任务《${title}》需要您审批，请及时查看");
                commonService.addSysMessage(operatorCode, messageVO);
            }
        });
    }

    @Override
    protected ActionType getActionType() {
        return ActionType.ADD;
    }

    @Override
    protected void createLog() {
    }
}
