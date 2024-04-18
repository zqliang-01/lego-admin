package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SysNoticeTemplateModifyVO extends VO {

    private String code;
    private String name;
    private String content;
    private List<String> employees;
    private List<String> depts;
}
