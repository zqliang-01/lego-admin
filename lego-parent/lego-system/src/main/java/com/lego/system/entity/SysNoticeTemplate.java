package com.lego.system.entity;

import com.lego.core.data.hibernate.BaseEntity;
import com.lego.core.util.DateUtil;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "sys_notice_template")
public class SysNoticeTemplate extends BaseEntity {

    private String content;
    private boolean published;
    private Date publishedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private SysEmployee creator;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "sys_notice_employee", joinColumns = {@JoinColumn(name = "notice_id")}, inverseJoinColumns = {@JoinColumn(name = "employee_id")})
    private List<SysEmployee> employees = new ArrayList<SysEmployee>();

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "sys_notice_dept", joinColumns = {@JoinColumn(name = "notice_id")}, inverseJoinColumns = {@JoinColumn(name = "dept_id")})
    private List<SysDept> depts = new ArrayList<SysDept>();

    protected SysNoticeTemplate() {
    }

    public SysNoticeTemplate(String name, String content, SysEmployee creator) {
        super(name);
        this.content = content;
        this.creator = creator;
    }

    @Override
    protected void doBuildReadableSnapshot(Map<String, String> attributes) {
        attributes.put("标题", this.getName());
        attributes.put("内容", StringUtil.toString(content));
        attributes.put("已发布", published ? "是" : "否");
        attributes.put("发布时间", DateUtil.toDateString(publishedTime));
        attributes.put("创建人", EntityUtil.toString(creator));
        attributes.put("关联员工", EntityUtil.toString(employees));
        attributes.put("关联部门", EntityUtil.toString(depts));
    }
}
