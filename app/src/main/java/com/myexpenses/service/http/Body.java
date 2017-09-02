package com.myexpenses.service.http;

import java.util.HashMap;
import java.util.Map;

public class Body {
    private Map<String, String> body;

    public Body() {
        body = new HashMap<>();
    }

    public void add(String key, String value) {
        body.put(key, value);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String param : body.keySet()) {
            sb.append(param).append("=").append(body.get(param)).append("&");
        }

        return sb.toString();
    }
}
