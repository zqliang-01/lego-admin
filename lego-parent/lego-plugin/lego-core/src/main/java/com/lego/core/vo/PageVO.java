package com.lego.core.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageVO extends VO {
    private static final long serialVersionUID = -6689108598804562601L;

    private int pageIndex = 1;

    private int pageSize = 10;
}
