package com.myexpenses.service.builder;

import com.myexpenses.model.Expense;

public class ExpenseBuilder {
    public String expenseId;
    public String expenseListId;
    public String spenderId;
    public String spenderName;
    public String categoryId;
    public String categoryName;
    public String amount;
    public String description;
    public String createdAt;

    public ExpenseBuilder withExpenseId(String expenseId) {
        this.expenseId = expenseId;

        return this;
    }

    public ExpenseBuilder withExpenseListId(String expenseListId) {
        this.expenseListId = expenseListId;

        return this;
    }

    public ExpenseBuilder withSpenderId(String spenderId) {
        this.spenderId = spenderId;

        return this;
    }

    public ExpenseBuilder withSpenderName(String spenderName) {
        this.spenderName = spenderName;

        return this;
    }

    public ExpenseBuilder withCategoryId(String categoryId) {
        this.categoryId = categoryId;

        return this;
    }

    public ExpenseBuilder withCategoryName(String categoryName) {
        this.categoryName = categoryName;

        return this;
    }

    public ExpenseBuilder withAmount(String amount) {
        this.amount = amount;

        return this;
    }

    public ExpenseBuilder withDescription(String description) {
        this.description = description;

        return this;
    }

    public ExpenseBuilder withCreatedAt(String createdAt) {
        this.createdAt = createdAt;

        return this;
    }

    public Expense build() {
        return new Expense(
            expenseId,
            expenseListId,
            spenderId,
            spenderName,
            categoryId,
            categoryName,
            amount,
            description,
            createdAt
        );
    }
}
