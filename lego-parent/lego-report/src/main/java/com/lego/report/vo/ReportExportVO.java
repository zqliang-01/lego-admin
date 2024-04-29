package com.lego.report.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Getter
@Setter
public class ReportExportVO extends VO {

    private String code;
    private String operatorCode;
    private String permissionCode;
    private Map<String, String> param;
    private HttpServletResponse response;
}
