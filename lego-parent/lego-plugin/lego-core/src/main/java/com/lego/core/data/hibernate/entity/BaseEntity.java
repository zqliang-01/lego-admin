package com.lego.core.data.hibernate.entity;

import com.lego.core.data.IdGenerator;
import com.lego.core.exception.CoreException;
import com.lego.core.util.DateUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @Getter
    private Long id;

    @Getter
    @Column(nullable = false, unique = true)
    protected String code;

    @Getter
    @Version
    private Integer version;

    @Getter
    @Setter
    protected String name;

    @Getter
    private Date createTime;

    protected BaseEntity() {
    }

    protected BaseEntity(String name) {
        this(null, name);
    }

    protected BaseEntity(String code, String name) {
        this.id = IdGenerator.getCurrent().nextId();
        this.code = (StringUtil.isBlank(code) ? id.toString() : code);
        this.name = name;
        this.version = 1;
        this.createTime = DateUtil.currentDateTime();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BaseEntity)) {
            return false;
        }
        BaseEntity baseEntity = (BaseEntity) obj;
        return this.getId().longValue() == baseEntity.getId().longValue();
    }

    @Override
    public int hashCode() {
        CoreException.check(id != null, "Type-id为空[{0}]", this.getClass().getName());
        return id.hashCode();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName());
        sb.append("[code=" + this.getCode());
        sb.append(",name=" + this.getName());
        sb.append("]");
        return sb.toString();
    }

    public final ReadableVO buildReadableSnapshot() {
        ReadableVO attributes = new ReadableVO();
        doBuildReadableSnapshot(attributes);
        return attributes;
    }

    public final String buildReadableSnapshotString() {
        ReadableVO attributes = buildReadableSnapshot();
        return attributes.toString();
    }

    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        CoreException.check(false, "未实现doBuildReadableSnapshot");
    }
}