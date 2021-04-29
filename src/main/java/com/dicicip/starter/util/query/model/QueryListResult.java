package com.dicicip.starter.util.query.model;

import java.util.List;

public class QueryListResult<D> {
    public List<D> data;
    public int count;

    public QueryListResult() {
    }

    public QueryListResult(List<D> data, int count) {
        this.data = data;
        this.count = count;
    }
}
