package com.lego.job.vo;

import com.lego.core.vo.PageVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobGroupSearchVO extends PageVO {

    private String appname;
    private String title;
}
