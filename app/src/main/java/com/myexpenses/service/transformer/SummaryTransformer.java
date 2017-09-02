package com.myexpenses.service.transformer;

import com.myexpenses.model.SpenderSummary;
import com.myexpenses.model.Summary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SummaryTransformer {
    public Summary fromJson(JSONObject payload) throws JSONException {
        return new Summary(
            getSpendersSummaries(payload.getJSONArray("spendersSummaries")),
            payload.getString("total")
        );
    }

    private SpenderSummary[] getSpendersSummaries(JSONArray payload) throws JSONException {
        SpenderSummary[] spendersSummaries = new SpenderSummary[payload.length()];

        for (int i = 0; i < payload.length(); i++) {
            spendersSummaries[i] = new SpenderSummaryTransformer().fromJson(payload.getJSONObject(i));
        }

        return spendersSummaries;
    }
}
