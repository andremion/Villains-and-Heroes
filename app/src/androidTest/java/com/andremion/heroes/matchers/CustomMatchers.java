package com.andremion.heroes.matchers;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class CustomMatchers {

    public static Matcher<View> withToolbarLayoutTitle(final Matcher<String> textMatcher) {
        return new BoundedMatcher<View, CollapsingToolbarLayout>(CollapsingToolbarLayout.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar layout title: ");
                textMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(CollapsingToolbarLayout toolbarLayout) {
                //noinspection ConstantConditions
                return textMatcher.matches(toolbarLayout.getTitle());
            }
        };
    }

}
