package com.lego.report.vo;

import com.lego.core.vo.PageVO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ReportOpenPageVO extends PageVO {

    private String permissionCode;
    private Map<String, String> param = new HashMap<>();
}
