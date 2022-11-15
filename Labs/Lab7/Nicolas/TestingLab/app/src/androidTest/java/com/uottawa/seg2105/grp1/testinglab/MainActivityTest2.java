package com.uottawa.seg2105.grp1.testinglab;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest2 {
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void firstNameIsInvalid() {
        onView(withId(R.id.editFirstName)).perform(typeText("user1"), closeSoftKeyboard());
        onView(withId(R.id.editLastName)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.editEmail)).perform(typeText("email@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.btnSubmit)).perform(click());
        onView(withText("Invalid First Name")).check(matches(isDisplayed()));
    }

    @Test
    public void lastNameIsInvalid() {
        onView(withId(R.id.editFirstName)).perform(typeText("user"), closeSoftKeyboard());
        onView(withId(R.id.editLastName)).perform(typeText("test1"), closeSoftKeyboard());
        onView(withId(R.id.editEmail)).perform(typeText("email@gmail.com1"), closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.btnSubmit)).perform(click());
        onView(withText("Invalid Last Name")).check(matches(isDisplayed()));
    }

    @Test
    public void emailIsInvalid() {
        onView(withId(R.id.editFirstName)).perform(typeText("user"), closeSoftKeyboard());
        onView(withId(R.id.editLastName)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.editEmail)).perform(typeText("email@gmail.com1"), closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.btnSubmit)).perform(click());
        onView(withText("Invalid Email")).check(matches(isDisplayed()));
    }

    @Test
    public void passwordIsInvalid() {
        onView(withId(R.id.editFirstName)).perform(typeText("user"), closeSoftKeyboard());
        onView(withId(R.id.editLastName)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.editEmail)).perform(typeText("email@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.editPassword)).perform(typeText("admin1"), closeSoftKeyboard());
        onView(withId(R.id.btnSubmit)).perform(click());
        onView(withText("Invalid Password")).check(matches(isDisplayed()));
    }
}
