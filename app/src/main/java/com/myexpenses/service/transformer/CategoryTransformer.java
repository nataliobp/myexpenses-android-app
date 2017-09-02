package com.myexpenses.service.transformer;

import com.myexpenses.model.Category;

import org.json.JSONException;
import org.json.JSONObject;

public class CategoryTransformer {
    public Category fromJson(JSONObject payload) throws JSONException {
        return new Category(
            payload.getString("categoryId"),
            payload.getString("name"),
            payload.getString("expenseListId")
        );
    }
}
