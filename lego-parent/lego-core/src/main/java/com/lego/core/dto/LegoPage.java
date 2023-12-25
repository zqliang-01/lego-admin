package com.lego.core.dto;

import java.util.ArrayList;
import java.util.List;

import com.lego.core.vo.PageVO;

import lombok.Getter;
import lombok.Setter;

/**
 * 与具体ORM实现无关的分页参数及查询结果封装. 注意所有序号从1开始.
 */
public class LegoPage<T> extends DTO {

    private static final long serialVersionUID = 8204782953867306849L;

    // -- 分页参数 --//
    @Getter
    private int pageIndex = 1; // 当前页：从1开始
    @Getter
    private int pageSize = 10; // 每页显示条数

    // -- 返回结果 --//
    @Getter
    @Setter
    private List<T> result = new ArrayList<T>();
    @Getter
    @Setter
    private int totalCount = 0; // 总记录条数

    public LegoPage(PageVO pageVO) {
    	this.pageIndex = pageVO.getPageIndex();
        this.pageSize = pageVO.getPageSize();
        this.totalCount = 0;
    }

    public LegoPage(List<T> result, long current, long pageSize, long totalCount) {
        this.pageIndex = (int) current;
        this.pageSize = (int) pageSize;
        this.result = result;
        this.totalCount = (int) totalCount;
    }
}
