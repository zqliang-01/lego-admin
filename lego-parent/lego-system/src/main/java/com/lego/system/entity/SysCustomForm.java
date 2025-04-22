package com.lego.system.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "sys_custom_form")
public class SysCustomForm extends BaseEntity {

    private boolean enable;
    private String simpleApiUrl;
    private String queryApiUrl;
    private String detailApiUrl;
    private String addApiUrl;
    private String updateApiUrl;
    private String deleteApiUrl;
    private String exportApiUrl;
    private String exportAllApiUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private SysGenTable table;

    protected SysCustomForm() {
    }

    public SysCustomForm(String code, String name) {
        super(code, name);
        this.enable = true;
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", getCode());
        attributes.put("名称", getName());
        attributes.put("数据表", EntityUtil.toString(table));
        attributes.put("查询Api", StringUtil.toString(queryApiUrl));
        attributes.put("详情Api", StringUtil.toString(detailApiUrl));
        attributes.put("简讯Api", StringUtil.toString(simpleApiUrl));
        attributes.put("新增Api", StringUtil.toString(addApiUrl));
        attributes.put("修改Api", StringUtil.toString(updateApiUrl));
        attributes.put("删除Api", StringUtil.toString(deleteApiUrl));
        attributes.put("导出Api", StringUtil.toString(exportApiUrl));
        attributes.put("导出全部Api", StringUtil.toString(exportAllApiUrl));
        attributes.put("状态", enable ? "生效" : "失效");
    }
}
