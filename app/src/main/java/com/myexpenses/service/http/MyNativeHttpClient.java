package com.myexpenses.service.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MyNativeHttpClient implements MyHttpClient {
    
    public MyNativeHttpClient() {
    }

    public Response get(Request request) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(request.url + "?" + request.body.toString()).openConnection();
        conn.connect();

        return new Response(conn.getResponseCode(), getResponseMessage(conn));
    }

    public Response post(Request request) throws IOException {
        return putOrPost(request, "POST");
    }

    public Response put(Request request) throws IOException {
        return putOrPost(request, "PUT");
    }

    public Response delete(Request request) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(request.url).openConnection();
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestMethod("DELETE");
        conn.connect();

        return new Response(conn.getResponseCode(), getResponseMessage(conn));
    }

    private Response putOrPost(Request request, String method) throws IOException {
        byte[] postData = request.body.toString().getBytes(StandardCharsets.UTF_8);
        HttpURLConnection conn = (HttpURLConnection) new URL(request.url).openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
        conn.setUseCaches(false);
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write(postData);
        }

        return new Response(conn.getResponseCode(), getResponseMessage(conn));
    }

    private String getResponseMessage(HttpURLConnection connection) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
        );

        StringBuilder content = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            content.append(line).append("\n");
        }

        bufferedReader.close();

        return content.toString();
    }

}
