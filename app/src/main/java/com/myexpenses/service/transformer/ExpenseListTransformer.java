package com.myexpenses.service.transformer;

import com.myexpenses.model.ExpenseList;

import org.json.JSONException;
import org.json.JSONObject;

public class ExpenseListTransformer {
    public ExpenseList fromJson(JSONObject payload) throws JSONException {
        return new ExpenseList(
            payload.getString("expenseListId"),
            payload.getString("name")
        );
    }

    public ExpenseList fromJson(String payload) throws JSONException {
        return fromJson(new JSONObject(payload));
    }

}
