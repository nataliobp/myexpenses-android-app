package com.myexpenses.service.http;

public class Request {
    public final String url;
    public final Body body;

    public Request(String url, Body body) {
        this.url = url;
        this.body = body;
    }

    public Request(String url) {
        this.url = url;
        this.body = new Body();
    }
}
