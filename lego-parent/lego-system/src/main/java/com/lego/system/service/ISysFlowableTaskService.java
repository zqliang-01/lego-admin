package com.lego.system.service;

import com.lego.system.dto.SysFlowableTaskInfo;

import java.util.Map;

public interface ISysFlowableTaskService {

    String start(String operatorCode, String definitionId, Map<String, Object> variables);

    SysFlowableTaskInfo findUndoBy(String employeeCode, String definitionId);

    void complete(String employeeCode, String id);

    String getBpmnXml(String definitionId);
}
