package com.myexpenses.activity.log_in;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myexpenses.R;
import com.myexpenses.model.Spender;

public class SpendersAdapter extends BaseAdapter {

    private Spender[] spenders;

    public int getCount() {
        return (null == spenders) ? 0 : spenders.length;
    }

    public Object getItem(int i) {
        return spenders[i];
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.spender_spinner_item, viewGroup, false);
        }

        TextView spenderTextView = view.findViewById(R.id.spenderName);
        Spender aSpender = (Spender) getItem(i);
        spenderTextView.setText(aSpender.name);

        return view;
    }

    public void setData(Spender[] spenders) {
        this.spenders = spenders;
        notifyDataSetChanged();
    }
}
