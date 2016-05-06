package com.andremion.heroes.actions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.v7.widget.SearchView;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

public class CustomActions {

    public static ViewAction typeQuery(final String value) {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(SearchView.class));
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((SearchView) view).setQuery(value, false);
            }

            @Override
            public String getDescription() {
                return String.format("type query(%s)", value);
            }
        };
    }

}
