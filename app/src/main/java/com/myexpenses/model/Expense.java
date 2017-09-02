package com.myexpenses.model;

public class Expense {
    public final String expenseId;
    public final String expenseListId;
    public final String spenderId;
    public final String spenderName;
    public final String categoryId;
    public final String categoryName;
    public final String amount;
    public final String description;
    public final String createdAt;

    public Expense(
        String anExpenseId,
        String anExpenseListId,
        String aSpenderId,
        String aSpenderName,
        String aCategoryId,
        String aCategoryName,
        String anAmount,
        String aDescription,
        String createdAt
    ) {
        expenseId = anExpenseId;
        expenseListId = anExpenseListId;
        spenderId = aSpenderId;
        spenderName = aSpenderName;
        categoryId = aCategoryId;
        categoryName = aCategoryName;
        amount = anAmount;
        description = aDescription;
        this.createdAt = createdAt;
    }
}
