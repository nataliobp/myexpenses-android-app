package com.myexpenses.service.http;

import java.io.IOException;

interface MyHttpClient {
    Response get(Request request) throws IOException;

    Response post(Request request) throws IOException;

    Response put(Request request) throws IOException;

    Response delete(Request request) throws IOException;

}
