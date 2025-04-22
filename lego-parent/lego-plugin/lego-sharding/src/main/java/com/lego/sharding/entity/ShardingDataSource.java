package com.lego.sharding.entity;

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
@Table(name = "sharding_data_source")
public class ShardingDataSource extends BaseEntity {

    private boolean enable;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private ShardingTemplate template;

    protected ShardingDataSource() { }

    public ShardingDataSource(String code, String name) {
        super(code, name);
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("生效", enable ? "是" : "否");
        attributes.put("描述", StringUtil.toString(description));
        attributes.put("模板", EntityUtil.toString(template));
    }
}
