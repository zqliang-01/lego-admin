package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class FlowableProcessNodeInfo extends DTO {

    private String xml;
    private String name;
    private Set<String> finishedTaskSet = new HashSet<>();
    private Set<String> finishedSequenceFlowSet = new HashSet<>();
    private Set<String> unfinishedTaskSet = new HashSet<>();
    private Set<String> rejectedTaskSet = new HashSet<>();

    public void addFinishedTask(String taskId) {
        finishedTaskSet.add(taskId);
    }

    public void addFinishedFlow(String flowId) {
        finishedSequenceFlowSet.add(flowId);
    }

}
