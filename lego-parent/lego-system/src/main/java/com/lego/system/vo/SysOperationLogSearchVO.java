package com.lego.system.vo;

import com.lego.core.vo.PageVO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SysOperationLogSearchVO extends PageVO {

    private static final long serialVersionUID = 1L;

    private String description;
    private String operatorCode;
    private Date startTime;
    private Date endTime;
}
