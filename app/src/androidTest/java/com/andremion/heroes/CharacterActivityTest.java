/*
 * Copyright (c) 2016. André Mion
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

package com.andremion.heroes;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.andremion.heroes.ui.character.view.CharacterActivity;
import com.andremion.heroes.util.DataConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.andremion.heroes.matchers.CustomMatchers.withToolbarLayoutTitle;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class CharacterActivityTest {

    @Rule
    public final ActivityTestRule mActivityRule = new ActivityTestRule<CharacterActivity>(CharacterActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = super.getActivityIntent();
            intent.putExtra(CharacterActivity.EXTRA_CHARACTER, DataConstants.CHARACTER);
            return intent;
        }
    };

    private String mUnavailableText;

    @Before
    public void setUp() {
        mUnavailableText = mActivityRule.getActivity().getString(R.string.unavailable_data);
    }

    @Test
    public void testLoad() throws InterruptedException {
        // Check if description View matches with expected value
        onView(withId(R.id.description)).check(matches(withText(is(mUnavailableText))));
        // Check if toolbar title matches with expected value
        onView(withId(R.id.toolbar_layout)).check(matches(withToolbarLayoutTitle(is(DataConstants.CHARACTER_NAME))));
    }

}