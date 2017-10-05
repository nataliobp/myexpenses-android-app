package com.myexpenses.model;

public class Category {

    public final String name;
    public final String categoryId;
    public final String expenseListId;

    public Category(String aCategoryId, String aName, String anExpenseListId) {
        name = aName;
        categoryId = aCategoryId;
        expenseListId = anExpenseListId;
    }

    public Category(String name, String expenseListId) {
        this(null, name, expenseListId);
    }
}
