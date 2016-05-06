package com.andremion.heroes;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.andremion.heroes.ui.character.CharacterActivity;
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
@LargeTest
public class CharacterActivityTest {

    @Rule
    public final ActivityTestRule mActivityRule = new ActivityTestRule<CharacterActivity>(CharacterActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = super.getActivityIntent();
            intent.putExtra(CharacterActivity.EXTRA_ID, DataConstants.CHARACTER_ID);
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