package com.lego.doc.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import com.lego.doc.entity.simpletype.DocFileLocation;
import com.lego.doc.entity.simpletype.DocFileType;
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
@Table(name = "doc_file")
public class DocFile extends BaseEntity {

    private long size;
    private String path;
    private boolean enable;
    private String creatorCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private DocFileType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private DocFileLocation location;

    protected DocFile() {
    }

    public DocFile(String name) {
        super(name);
        this.enable = true;
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("大小（字节）", StringUtil.toString(size));
        attributes.put("存储路径", StringUtil.toString(path));
        attributes.put("生效", enable ? "是" : "否");
        attributes.put("类型", EntityUtil.toString(type));
        attributes.put("创建人", StringUtil.toString(creatorCode));
        attributes.put("存储位置", EntityUtil.toString(location));
    }
}
