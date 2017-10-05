package com.myexpenses.activity.expense_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myexpenses.R;
import com.myexpenses.model.ExpenseList;

public class ExpenseListAdapter extends BaseAdapter {

    private ExpenseList[] expenseLists;
    private ExpenseListClickListener clickListener;

    public ExpenseListAdapter(ExpenseListClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setData(ExpenseList[] expenseLists) {
        this.expenseLists = expenseLists;
        notifyDataSetChanged();
    }

    public int getCount() {
        return (null == expenseLists) ? 0 : expenseLists.length;
    }

    public Object getItem(int i) {
        return expenseLists[i];
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ExpenseList anExpenseList = (ExpenseList) getItem(i);

        if (view == null) {
            view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.expense_list_item, viewGroup, false);

            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    clickListener.onItemClick(anExpenseList);
                }
            });
        }

        TextView expenseListView = view.findViewById(R.id.expense_list_name);
        expenseListView.setText(anExpenseList.name);

        return view;
    }

    interface ExpenseListClickListener {
        void onItemClick(ExpenseList anExpenseList);
    }
}
