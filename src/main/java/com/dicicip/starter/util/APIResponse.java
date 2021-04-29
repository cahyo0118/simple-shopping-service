package com.dicicip.starter.util;

public class APIResponse<D> {
    public boolean success = true;
    public D data;
    public String message = "Successfully executed action";

    public APIResponse() {
    }

    public APIResponse(D data) {
        this.data = data;
    }

    public APIResponse(D data, boolean success, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

}
