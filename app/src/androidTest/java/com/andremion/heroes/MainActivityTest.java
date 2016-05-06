package com.andremion.heroes;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.andremion.heroes.ui.home.MainActivity;
import com.andremion.heroes.util.DataConstants;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public final ActivityTestRule mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testExpectedItem() {
        // Check if the list is displayed
        onView(withId(R.id.characters)).check(matches(isDisplayed()));
        // Check if there is one View item with expected value
        //noinspection unchecked
        onView(allOf(isDescendantOfA(withId(R.id.characters)), withText(is(DataConstants.CHARACTER_NAME))));
    }

    @Test
    public void testListItemClick() {
        // Check if the list is displayed
        onView(withId(R.id.characters)).check(matches(isDisplayed()));
        // Perform click on first item
        onView(withId(R.id.characters)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void testSearchMenuItemClick() {
        // Perform click on search menu item
        onView(withId(R.id.action_search)).perform(click());
    }
}