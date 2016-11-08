package com.andremion.heroes.ui.util;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.View;

public class ViewUtils {

    private ViewUtils() {
    }

    public static void fadeView(View view, boolean show, boolean animate) {

        // Cancel any on-going animation
        ViewCompat.animate(view).cancel();

        if (show) {
            view.setVisibility(View.VISIBLE);
            if (animate) {
                ViewCompat.setAlpha(view, 0f);
                ViewCompat.animate(view).alpha(1f).start();
            } else {
                ViewCompat.setAlpha(view, 1f);
            }
        } else {
            if (view.getVisibility() == View.VISIBLE) {
                if (animate) {
                    ViewCompat.animate(view)
                            .alpha(0f)
                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(View view) {
                                    ViewCompat.animate(view).setListener(null);
                                    view.setVisibility(View.INVISIBLE);
                                }
                            }).start();
                } else {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }

    }
}
