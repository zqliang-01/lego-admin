package com.lego.job.vo;

import com.lego.core.vo.PageVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobConfigSearchVO extends PageVO {

    private int jobGroup;
    private int triggerStatus;
    private String jobDesc;
    private String executorHandler;
    private String author;
}
