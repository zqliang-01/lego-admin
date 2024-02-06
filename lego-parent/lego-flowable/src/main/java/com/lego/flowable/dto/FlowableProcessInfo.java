package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FlowableProcessInfo extends DTO {

    /**
     * 流程定义ID
     */
    private String definitionId;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 流程Key
     */
    private String processKey;

    /**
     * 分类编码
     */
    private String category;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 部署ID
     */
    private String deploymentId;

    /**
     * 流程定义状态: 1:激活 , 2:中止
     */
    private Boolean suspended;

    /**
     * 部署时间
     */
    private Date deploymentTime;

}
