package com.myexpenses.model;

public class Summary {
    public final SpenderSummary[] spendersSummaries;
    public final String totalSpent;

    public Summary(SpenderSummary[] spendersSummaries, String totalSpent) {
        this.spendersSummaries = spendersSummaries;
        this.totalSpent = totalSpent;
    }
}
