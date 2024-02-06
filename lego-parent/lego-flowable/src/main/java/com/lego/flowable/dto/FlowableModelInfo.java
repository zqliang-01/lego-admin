package com.lego.flowable.dto;

import com.lego.core.dto.DTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FlowableModelInfo extends DTO {

    private static final long serialVersionUID = 1L;

    /**
     * 模型ID
     */
    private String id;
    /**
     * 模型名称
     */
    private String name;
    /**
     * 模型Key
     */
    private String key;
    /**
     * 分类编码
     */
    private String category;
    /**
     * 版本
     */
    private String version;
    /**
     * 模型描述
     */
    private String description;
    /**
     * 创建时间
     */
    private Date createTime;
}
