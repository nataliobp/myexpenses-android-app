package com.myexpenses.model;

public class ExpenseListReport {
    public final String expenseListId;
    public final Expense[] expenses;
    public final Summary summary;

    public ExpenseListReport(
        String anExpenseListId,
        Summary aSummary,
        Expense[] expenses
    ) {
        expenseListId = anExpenseListId;
        summary = aSummary;
        this.expenses = expenses;
    }
}
