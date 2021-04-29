package com.dicicip.starter.util;

public class APIListResponse<D> {
    public boolean success = true;
    public D data;
    public String message = "Successfully executed action";
    public int from = 0;
    public int to;
    public int total;

    public APIListResponse() {
    }

    public APIListResponse(D data) {
        this.data = data;
    }

    public APIListResponse(D data, boolean success, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public APIListResponse(boolean success, D data, String message, int from, int to, int total) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.from = from;
        this.to = to;
        this.total = total;
    }
}
