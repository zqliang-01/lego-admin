package com.lego.job.dto;

import com.lego.core.dto.DTO;
import com.lego.job.core.model.XxlJobGroup;
import com.lego.job.core.model.XxlJobInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobLogInfo extends DTO {

    private XxlJobInfo jobInfo;
    private List<XxlJobGroup> jobGroupList;
}
