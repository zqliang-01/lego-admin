package com.lego.flowable.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lego.flowable.dto.FlowableDefinitionInfo;

public interface FlowableDefinitionMapper {

    IPage<FlowableDefinitionInfo> selectByName(String name, IPage page);
}
