package com.myexpenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myexpenses.model.Category;

public class CategoriesAdapter extends BaseAdapter {

    private Category[] categories;

    public int getCount() {
        return (null == categories) ? 0 : categories.length;
    }

    public Object getItem(int i) {
        return categories[i];
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.category_spinner_item, viewGroup, false);
        }

        TextView categoryTextView = view.findViewById(R.id.categoryName);
        Category aCategory = (Category) getItem(i);
        categoryTextView.setText(aCategory.name);

        return view;
    }

    public void setData(Category[] categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public int getIndexOfCategory(String aCategoryId) {
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].categoryId.equals(aCategoryId)) {
                return i;
            }
        }

        return 0;
    }
}
