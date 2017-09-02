package com.myexpenses.service.http;

import com.myexpenses.model.Expense;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MyExpensesApiClient {
    private final String BASE_URL = "http://10.0.2.2:8080";

    public Response addExpense(Expense anExpense) throws IOException {
        Body body = new Body();
        body.add("expense_list_id", anExpense.expenseListId);
        body.add("spender_id", anExpense.spenderId);
        body.add("category_id", anExpense.categoryId);
        body.add("amount", anExpense.amount);
        body.add("description", anExpense.description);

        return post(new Request("/expense", body));
    }

    public Response alterExpense(Expense anExpense) throws IOException {
        Body body = new Body();
        body.add("category_id", anExpense.categoryId);
        body.add("amount", anExpense.amount);
        body.add("description", anExpense.description);

        return put(new Request(String.format("/expense/%s", anExpense.expenseId), body));
    }

    public Response removeAnExpense(String anExpenseId) throws IOException {
        return delete(new Request(String.format("/expense/%s", anExpenseId)));
    }

    public Response getAnExpense(String expenseId) throws IOException {
        return get(new Request(String.format("/expense/%s", expenseId)));
    }

    public Response getAnExpenseListReport(String expenseListId) throws IOException {
        return get(new Request(String.format("/expense_list/%s/report", expenseListId)));
    }

    public Response getCategoriesOfExpenseListOfId(String expenseListId) throws IOException {
        return get(new Request(String.format("/expense_list/%s/categories", expenseListId)));
    }

    public Response getSpenders() throws IOException {
        return get(new Request("/spenders"));
    }

    private Response get(Request request) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + request.url + "?" + request.body.toString()).openConnection();
        conn.connect();

        return new Response(conn.getResponseCode(), getResponseMessage(conn));
    }

    private Response post(Request request) throws IOException {
        return putOrPost(request, "POST");
    }

    private Response put(Request request) throws IOException {
        return putOrPost(request, "PUT");
    }

    private Response putOrPost(Request request, String method) throws IOException {
        byte[] postData = request.body.toString().getBytes(StandardCharsets.UTF_8);
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + request.url).openConnection();
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

    private Response delete(Request request) throws IOException {
        URL url = new URL(request.url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestMethod("DELETE");
        conn.connect();

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
