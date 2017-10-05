package com.myexpenses.model;

public class ExpenseList {

    public final String name;
    public final String expenseListId;

    public ExpenseList(String anExpenseListId, String aName) {
        name = aName;
        expenseListId = anExpenseListId;
    }

    public ExpenseList(String name) {
       this(null, name);
    }
}
