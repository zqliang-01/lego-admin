package com.lego.core.vo;

import com.lego.core.module.flowable.FlowableCheckStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVO extends VO {

    private String checkStatus = FlowableCheckStatus.completed;

}
