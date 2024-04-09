package com.lego.system.vo;

import com.lego.core.vo.PageVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysMessageSearchVO extends PageVO {

    private Boolean readed;
    private String type;
}
