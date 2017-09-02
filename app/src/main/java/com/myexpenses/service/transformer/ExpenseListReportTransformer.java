package com.myexpenses.service.transformer;

import com.myexpenses.model.Expense;
import com.myexpenses.model.ExpenseListReport;
import com.myexpenses.model.Summary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExpenseListReportTransformer {
    public ExpenseListReport fromJson(JSONObject payload) throws JSONException {
        return new ExpenseListReport(
            payload.getString("expenseListId"),
            getSummary(payload.getJSONObject("summary")),
            getExpenses(payload.getJSONArray("expenses"))
        );
    }

    private Expense[] getExpenses(JSONArray expensesPayload) throws JSONException {
        Expense[] expenses = new Expense[expensesPayload.length()];

        for (int i = 0; i < expensesPayload.length(); i++) {
            expenses[i] = new ExpenseTransformer().fromJson(expensesPayload.getJSONObject(i));
        }

        return expenses;
    }

    private Summary getSummary(JSONObject payload) throws JSONException {
        return new SummaryTransformer().fromJson(payload);
    }

}
