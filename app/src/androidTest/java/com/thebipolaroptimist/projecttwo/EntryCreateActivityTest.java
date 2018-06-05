package com.thebipolaroptimist.projecttwo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;


@RunWith(JUnit4.class)
public class EntryCreateActivityTest {

    @Rule
    public ActivityTestRule<EntryCreateActivity> activityRule =
            new ActivityTestRule<>(EntryCreateActivity.class, true, false);

    @Test
    public void testSomething()
    {
        Resources resources = getInstrumentation().getTargetContext().getResources();
        int actionBarId = resources.getIdentifier("action_bar_container", "id", "android");
        activityRule.launchActivity(null);

        onView(withId(actionBarId)).check(matches(isDisplayed()));
        //onView(allOf(instanceOf(TextView.class), withParent(withId(actionBarId))))
           //     .check(matches(withText("Entry:")));
    }
}