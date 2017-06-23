package com.example.android.bakingapp;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.Activities.RecipeDetailsActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Etman on 6/23/2017.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityTest {

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> activityActivityTestRule =
            new ActivityTestRule<>(RecipeDetailsActivity.class, true, false);

    @Test
    public void checkTextViews() {
        onView(allOf(withId(R.id.ingredients_title), withText(R.string.ingredients), isDisplayed()));
        onView(allOf(withId(R.id.steps_title), withText(R.string.steps), isDisplayed()));
    }

    @Test
    public void checkRecyclerViews() {
        onView(allOf(withId(R.id.ingredients_list), isDisplayed()));
        onView(allOf(withId(R.id.steps_list), isDisplayed()));
    }

}
