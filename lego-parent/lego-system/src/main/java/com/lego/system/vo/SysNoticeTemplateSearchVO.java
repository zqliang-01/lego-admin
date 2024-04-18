package com.lego.system.vo;

import com.lego.core.vo.PageVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysNoticeTemplateSearchVO extends PageVO {

    private String name;
    private Boolean published;
}
