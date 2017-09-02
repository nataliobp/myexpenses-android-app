package com.myexpenses.service.http;


public class Response {
    public final int code;
    public final String message;

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
