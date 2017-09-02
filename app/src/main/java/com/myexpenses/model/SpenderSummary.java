package com.myexpenses.model;

public class SpenderSummary {
    public final String spenderId;
    public final String total;
    public final String balance;
    public final String spenderName;

    public SpenderSummary(String aSpenderId, String aSpenderName, String aTotal, String aBalance) {
        spenderId = aSpenderId;
        spenderName = aSpenderName;
        total = aTotal;
        balance = aBalance;
    }
}
