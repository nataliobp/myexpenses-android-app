package com.myexpenses.service.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.TextView;

import com.myexpenses.R;

public class InitialDrawer {
    public static void drawInitial(String spenderName, TextView initialView) {
        String initial = String.valueOf(spenderName.toUpperCase().charAt(0));
        initialView.setText(initial);
        Drawable backgroundDrawable = DrawableCompat.wrap(initialView.getBackground()).mutate();
        DrawableCompat.setTint(backgroundDrawable, getColorOfInitial(initial, initialView.getContext()));
    }

    private static int getColorOfInitial(String initial, Context context) {

        switch (initial) {
            case "N":
                return ContextCompat.getColor(context, R.color.blue);
            case "T":
                return ContextCompat.getColor(context, R.color.orange);
        }

        return android.graphics.Color.GRAY;
    }

}
