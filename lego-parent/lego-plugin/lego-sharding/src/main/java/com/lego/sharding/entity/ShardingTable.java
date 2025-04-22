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
@Table(name = "sharding_table")
public class ShardingTable extends BaseEntity {

    private boolean enable;
    private String description;
    private String logicTableName;
    private String actualDataNodes;
    private String shardingColumn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "algorithm_id")
    private ShardingAlgorithm algorithm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_source_id")
    private ShardingDataSource dataSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private ShardingTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id")
    private ShardingConfig config;

    protected ShardingTable() {
    }

    public ShardingTable(String code, String name) {
        super(code, name);
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("生效", enable ? "是" : "否");
        attributes.put("描述", StringUtil.toString(description));
        attributes.put("逻辑表名称", StringUtil.toString(logicTableName));
        attributes.put("物理表表达式", StringUtil.toString(actualDataNodes));
        attributes.put("本片字段", StringUtil.toString(shardingColumn));
        attributes.put("算法", EntityUtil.toString(algorithm));
        attributes.put("模板", EntityUtil.toString(template));
        attributes.put("规则", EntityUtil.toString(config));
        attributes.put("数据源", EntityUtil.toString(dataSource));
    }
}
