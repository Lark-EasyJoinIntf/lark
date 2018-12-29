package com.entity.entities;

import lombok.Data;

import java.util.List;

@Data
public class Page<R> {
    private int totalCount;
    private int totalPage;
    private int pageSize = 10;
    private int currPage = 1;
    private List<R> data;

    public Page(){}

    public Page(int totalCount, List<R> data){
        this.data = data;
        this.setTotalCount(totalCount);
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        this.totalPage = totalCount / pageSize + (totalCount % pageSize > 0 ? 1 : 0);
    }
}
