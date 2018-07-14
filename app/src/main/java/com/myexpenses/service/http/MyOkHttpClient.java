package com.myexpenses.service.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MyOkHttpClient implements MyHttpClient{
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient httpClient;
    private final Gson gson;

    public MyOkHttpClient() {
        this.httpClient = new OkHttpClient();
        gson = new GsonBuilder().create();
    }

    @Override
    public Response get(Request request) throws IOException {
        okhttp3.Request okRequest = new okhttp3.Request.Builder()
                .url(request.url)
                .get()
                .build();

        return performRequest(okRequest);
    }

    @Override
    public Response post(Request request) throws IOException {
        okhttp3.Request okRequest = new okhttp3.Request.Builder()
                .url(request.url)
                .post(RequestBody.create(JSON, gson.toJson(request.body)))
                .build();

        return performRequest(okRequest);
    }

    @Override
    public Response put(Request request) throws IOException {
        okhttp3.Request okRequest = new okhttp3.Request.Builder()
                .url(request.url)
                .put(RequestBody.create(JSON, gson.toJson(request.body)))
                .build();

        return performRequest(okRequest);
    }

    @Override
    public Response delete(Request request) throws IOException {
        okhttp3.Request okRequest = new okhttp3.Request.Builder()
                .url(request.url)
                .delete()
                .build();

        return performRequest(okRequest);
    }

    private Response performRequest(okhttp3.Request request) throws IOException {
        okhttp3.Response response = httpClient.newCall(request).execute();
        String responseBody = response.body() != null ? response.body().string() : "";

        return new Response(response.code(), responseBody);
    }
}
