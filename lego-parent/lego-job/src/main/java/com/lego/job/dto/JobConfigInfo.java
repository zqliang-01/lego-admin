package com.lego.job.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import com.lego.job.core.model.XxlJobGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobConfigInfo extends DTO {

    private List<TypeInfo> glueTypes;
    private List<TypeInfo> scheduleTypes;
    private List<TypeInfo> misfireStrategies;
    private List<TypeInfo> executorRouteStrategies;
    private List<TypeInfo> executorBlockStrategies;

    private List<XxlJobGroup> jobGroupList;
}
