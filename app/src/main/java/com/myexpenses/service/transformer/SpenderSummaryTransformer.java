package com.myexpenses.service.transformer;

import com.myexpenses.model.SpenderSummary;

import org.json.JSONException;
import org.json.JSONObject;

public class SpenderSummaryTransformer {
    public SpenderSummary fromJson(JSONObject payload) throws JSONException {
        return new SpenderSummary(
            payload.getJSONObject("spender").getString("spenderId"),
            payload.getJSONObject("spender").getString("name"),
            payload.getString("total"),
            payload.getString("balance")
        );
    }
}
