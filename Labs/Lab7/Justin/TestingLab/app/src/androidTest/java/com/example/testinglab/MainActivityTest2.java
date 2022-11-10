package com.example.testinglab;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest2 {
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void emailIsInvalid() {
        onView(withId(R.id.firstNameEdit)).perform(typeText("user"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.lastNameEdit)).perform(typeText("test"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.emailEdit)).perform(typeText("email@"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordEdit)).perform(typeText("pass"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.submitBtn)).perform(ViewActions.click());
        onView(withText("Invalid Email")).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void firstIsInvalid() {
        onView(withId(R.id.firstNameEdit)).perform(typeText("user1"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.submitBtn)).perform(ViewActions.click());
        onView(withText("Invalid First Name")).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void lastIsInvalid() {
        onView(withId(R.id.firstNameEdit)).perform(typeText("user"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.lastNameEdit)).perform(typeText("test1"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.submitBtn)).perform(ViewActions.click());
        onView(withText("Invalid Last Name")).check(matches(ViewMatchers.isDisplayed()));
    }
}
