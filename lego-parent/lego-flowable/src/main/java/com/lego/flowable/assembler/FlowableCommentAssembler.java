package com.lego.flowable.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.flowable.dto.FlowableCommentInfo;
import com.lego.flowable.vo.FlowableCommentType;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.history.HistoricTaskLogEntry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlowableCommentAssembler extends BaseAssembler<FlowableCommentInfo, Comment> {

    @Override
    protected FlowableCommentInfo doCreate(Comment entity) {
        FlowableCommentInfo info = new FlowableCommentInfo();
        info.setId(entity.getId());
        info.setContent(entity.getFullMessage());
        info.setCreateTime(entity.getTime());
        info.setType(FlowableCommentType.createTypeInfoBy(entity.getType()));
        return info;
    }

    public List<FlowableCommentInfo> createByLog(List<HistoricTaskLogEntry> taskLogs) {
        List<FlowableCommentInfo> infos = new ArrayList<>();
        for (HistoricTaskLogEntry taskLog : taskLogs) {
            FlowableCommentInfo info = new FlowableCommentInfo();
            info.setId(taskLog.getTaskId());
            info.setType(FlowableCommentType.createRejectTypeInfo());
            info.setCreateTime(taskLog.getTimeStamp());
            info.setContent(taskLog.getData());
            infos.add(info);
        }
        return infos;
    }
}
