package com.myexpenses.activity.expense_list_report;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myexpenses.R;
import com.myexpenses.model.SpenderSummary;
import com.myexpenses.model.Summary;
import com.myexpenses.service.ui.InitialDrawer;

import java.util.Locale;

public class SummaryAdapter extends BaseAdapter {

    private SpenderSummary[] spendersSummaries;

    public void setData(Summary aSummary) {
        spendersSummaries = aSummary.spendersSummaries;
        notifyDataSetChanged();
    }

    public int getCount() {
        return (null == spendersSummaries) ? 0 : spendersSummaries.length;
    }

    public Object getItem(int i) {
        return spendersSummaries[i];
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.summary_list_item, viewGroup, false);
        }
        SpenderSummary aSpenderSummary = (SpenderSummary) getItem(i);

        TextView spenderSummaryView = view.findViewById(R.id.spender_summary);

        spenderSummaryView.setText(
            String.format(
                Locale.forLanguageTag("es_ES"),
                "%.2f€",
                Double.valueOf(aSpenderSummary.total)
            )
        );

        TextView balanceView = view.findViewById(R.id.balance_summary);
        balanceView.setText(String.format(
            Locale.forLanguageTag("es_ES"),
            "%.2f€",
            Double.valueOf(aSpenderSummary.balance)
        ));

        InitialDrawer.drawInitial(aSpenderSummary.spenderName, (TextView) view.findViewById(R.id.initialSummary));

        return view;
    }
}
