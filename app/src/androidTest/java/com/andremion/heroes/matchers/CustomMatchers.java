/*
 * Copyright (c) 2017. André Mion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
