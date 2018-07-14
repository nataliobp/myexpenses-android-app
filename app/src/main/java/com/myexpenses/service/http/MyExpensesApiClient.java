package com.myexpenses.service.http;

import com.myexpenses.model.Category;
import com.myexpenses.model.Expense;
import com.myexpenses.model.ExpenseList;
import com.myexpenses.model.Spender;

import java.io.IOException;

public class MyExpensesApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8080";

    private final MyHttpClient httpClient;

    public MyExpensesApiClient() {
        this.httpClient = new MyOkHttpClient();
    }

    public Response addExpense(Expense anExpense) throws IOException {
        Body body = new Body();
        body.add("expense_list_id", anExpense.expenseListId);
        body.add("spender_id", anExpense.spenderId);
        body.add("category_id", anExpense.categoryId);
        body.add("amount", anExpense.amount);
        body.add("description", anExpense.description);

        return httpClient.post(new Request("/expense", body));
    }

    public Response alterExpense(Expense anExpense) throws IOException {
        Body body = new Body();
        body.add("category_id", anExpense.categoryId);
        body.add("amount", anExpense.amount);
        body.add("description", anExpense.description);

        return httpClient.put(new Request(url("/expense/%s", anExpense.expenseId), body));
    }

    public Response removeAnExpense(String anExpenseId) throws IOException {
        return httpClient.delete(new Request(url("/expense/%s", anExpenseId)));
    }

    public Response getAnExpense(String expenseId) throws IOException {
        return httpClient.get(new Request(url("/expense/%s", expenseId)));
    }

    public Response getAnExpenseListReport(String expenseListId) throws IOException {
        return httpClient.get(new Request(url("/expense_list/%s/report", expenseListId)));
    }

    public Response getCategoriesOfExpenseListOfId(String expenseListId) throws IOException {
        return httpClient.get(new Request(url("/expense_list/%s/categories", expenseListId)));
    }

    public Response createCategory(Category aCategory) throws IOException {
        Body body = new Body();
        body.add("name", aCategory.name);
        body.add("expense_list_id", aCategory.expenseListId);

        return httpClient.post(new Request(url("/category"), body));
    }

    public Response getSpenders() throws IOException {
        return httpClient.get(new Request(url("/spenders")));
    }

    public Response registerSpender(Spender aSpender) throws IOException {
        Body body = new Body();
        body.add("name", aSpender.name);
        body.add("email", aSpender.email);

        return httpClient.post(new Request(url("/spender", body)));
    }

    public Response getExpenseLists() throws IOException {
        return httpClient.get(new Request(url("/expense_lists")));
    }

    public Response startExpenseList(ExpenseList anExpenseList) throws IOException {
        Body body = new Body();
        body.add("name", anExpenseList.name);

        return httpClient.post(new Request(url("/expense_list"), body));
    }

    private String url(String suffix, Object ...params){
        return BASE_URL + String.format(suffix, params);
    }
}
