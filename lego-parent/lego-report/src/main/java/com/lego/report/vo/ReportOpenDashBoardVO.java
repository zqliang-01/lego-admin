package com.lego.report.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ReportOpenDashBoardVO extends VO {

    private String code;
    private Map<String, String> param = new HashMap<>();
}
