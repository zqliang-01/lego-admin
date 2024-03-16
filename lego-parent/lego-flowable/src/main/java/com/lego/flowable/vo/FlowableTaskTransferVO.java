package com.lego.flowable.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlowableTaskTransferVO extends VO {

    private String id;
    private String comment;
    private String employeeCode;
}
