package com.lego.system.vo;

import com.lego.core.vo.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SysNoticeTemplateCreateVO extends VO {

    private String name;
    private String content;
    private List<String> employees;
    private List<String> depts;
}
