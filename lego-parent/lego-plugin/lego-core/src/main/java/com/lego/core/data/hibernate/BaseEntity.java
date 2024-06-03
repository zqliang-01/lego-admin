package com.lego.core.data.hibernate;

import com.lego.core.data.IdGenerator;
import com.lego.core.exception.CoreException;
import com.lego.core.util.DateUtil;
import com.lego.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.text.MessageFormat.format;

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

    public final Map<String, String> buildReadableSnapshot() {
        LinkedHashMap<String, String> attributes = new LinkedHashMap<String, String>();
        doBuildReadableSnapshot(attributes);
        return attributes;
    }

    public final String buildReadableSnapshotString() {
        Map<String, String> attributes = buildReadableSnapshot();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            sb.append(format("{0}:{1}\r\n", entry.getKey(), entry.getValue()));
            i++;
            if (i < attributes.entrySet().size()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        CoreException.check(false, "未实现doBuildReadableSnapshot");
    }
}
