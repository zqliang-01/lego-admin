package com.lego.job.vo;

import com.lego.core.vo.PageVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobLogSearchVO extends PageVO {

    private int jobGroup;
    private int jobId;
    private int logStatus;
    private String filterTime;
}
