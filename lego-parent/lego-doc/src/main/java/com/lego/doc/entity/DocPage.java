package com.lego.doc.entity;

import com.lego.core.data.hibernate.entity.BaseEntity;
import com.lego.core.util.EntityUtil;
import com.lego.core.util.StringUtil;
import com.lego.core.vo.ReadableVO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Table(name = "doc_page")
public class DocPage extends BaseEntity {

    private boolean enable;
    private String content;
    private String creatorCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private DocBook book;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private Set<DocPageUser> pageUsers = new HashSet<>();

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private Set<DocPageDept> pageDepts = new HashSet<>();

    protected DocPage() {
    }

    public DocPage(String name, String creatorCode) {
        super(name);
        this.creatorCode = creatorCode;
    }

    public boolean checkPermission(String operator) {
        for (DocPageUser pageUser : pageUsers) {
            if (StringUtil.equals(pageUser.getUserCode(), operator)) {
                return true;
            }
        }
        return book.checkPermission(operator);
    }

    @Override
    protected void doBuildReadableSnapshot(ReadableVO attributes) {
        attributes.put("编码", StringUtil.toString(code));
        attributes.put("名称", StringUtil.toString(name));
        attributes.put("是否生效", enable ? "是" : "否");
        attributes.put("内容", StringUtil.getMD5(content));
        attributes.put("图书", EntityUtil.toString(book));
        attributes.put("参与人", StringUtil.toString(getUserCodes()));
        attributes.put("参与部门", StringUtil.toString(getDeptCodes()));
    }

    public List<String> getUserCodes() {
        return this.pageUsers.stream().map(u -> u.getUserCode()).collect(Collectors.toList());
    }

    public List<String> getDeptCodes() {
        return this.pageDepts.stream().map(u -> u.getDeptCode()).collect(Collectors.toList());
    }
}
