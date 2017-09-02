package com.myexpenses.service.transformer;

import com.myexpenses.model.Spender;

import org.json.JSONException;
import org.json.JSONObject;

public class SpenderTransformer {
    public Spender fromJson(JSONObject payload) throws JSONException {
        return new Spender(
            payload.getString("spenderId"),
            payload.getString("name"),
            payload.getString("email")
        );
    }
}
