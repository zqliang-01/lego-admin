package com.lego.flowable.mapper;

import org.flowable.engine.task.Comment;

import java.util.List;

public interface FlowableCommentMapper {

    List<Comment> selectCommentsByTaskId(String taskId);
}
