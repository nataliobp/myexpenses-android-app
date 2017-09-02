package com.myexpenses.service.transformer;

import com.myexpenses.model.Expense;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ExpenseTransformer {

    public Expense fromJson(JSONObject expensePayload) throws JSONException {
        return new Expense(
            expensePayload.getString("expenseId"),
            expensePayload.getString("expenseListId"),
            expensePayload.getJSONObject("spender").getString("spenderId"),
            expensePayload.getJSONObject("spender").getString("name"),
            expensePayload.getJSONObject("category").getString("categoryId"),
            expensePayload.getJSONObject("category").getString("name"),
            expensePayload.getString("amount"),
            expensePayload.getString("description"),
            expensePayload.getString("createdAt")
        );
    }

    public Expense fromJson(String rawJson) throws JSONException {
        return fromJson(new JSONObject(new JSONTokener(rawJson)));
    }
}
