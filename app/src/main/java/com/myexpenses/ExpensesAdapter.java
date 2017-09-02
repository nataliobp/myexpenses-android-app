package com.myexpenses;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myexpenses.model.Expense;
import com.myexpenses.service.ui.InitialDrawer;

import java.util.Locale;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpenseViewHolder> {

    private Expense[] expenses = null;
    private ExpenseListClickListener clickListener;

    public ExpensesAdapter(ExpenseListClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setData(Expense[] expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ExpenseViewHolder(
            LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.expense_list_item, parent, false));
    }

    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        Expense anExpense = expenses[position];
        holder.amountView.setText(String.format(Locale.forLanguageTag("es_ES"), "%.2f€", Double.valueOf(anExpense.amount)));
        holder.descriptionView.setText(anExpense.categoryName);
        InitialDrawer.drawInitial(anExpense.spenderName, holder.initialView);

    }

    public int getItemCount() {
        return (expenses == null) ? 0 : expenses.length;
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView amountView;
        TextView initialView;
        TextView descriptionView;

        ExpenseViewHolder(View itemView) {
            super(itemView);
            itemView.setBackgroundResource(R.drawable.circle);
            itemView.setOnClickListener(this);

            amountView = itemView.findViewById(R.id.amount);
            initialView = itemView.findViewById(R.id.initial);
            descriptionView = itemView.findViewById(R.id.description);
        }

        public void onClick(View view) {
            clickListener.onItemClick(expenses[getAdapterPosition()]);
        }
    }

    interface ExpenseListClickListener {
        void onItemClick(Expense anExpense);
    }
}
