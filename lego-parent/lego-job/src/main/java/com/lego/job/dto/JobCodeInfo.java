package com.lego.job.dto;

import com.lego.core.dto.DTO;
import com.lego.core.dto.TypeInfo;
import com.lego.job.core.model.XxlJobInfo;
import com.lego.job.core.model.XxlJobLogGlue;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobCodeInfo extends DTO {

    private XxlJobInfo jobInfo;
    private List<XxlJobLogGlue> jobLogGlues;
    private List<TypeInfo> glueTypes;
}
