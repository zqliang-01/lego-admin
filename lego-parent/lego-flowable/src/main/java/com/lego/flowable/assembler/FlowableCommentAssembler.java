package com.lego.flowable.assembler;

import com.lego.core.assembler.BaseAssembler;
import com.lego.flowable.dto.FlowableCommentInfo;
import com.lego.flowable.vo.FlowableCommentType;
import org.flowable.engine.task.Comment;
import org.springframework.stereotype.Component;

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
}
