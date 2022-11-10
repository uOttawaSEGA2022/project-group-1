package com.example.testinglab;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.widget.TextView;

import androidx.test.annotation.UiThreadTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
    private MainActivity mActivity = null;
    private TextView text;

    /*@Before
    public void setUp() throws Exception {
        mActivity=mActivityTestRule.onActivity(m);
    }*/

    @Test
    @UiThreadTest
    public void checkFirstName() throws Exception {

        activityScenarioRule.getScenario().onActivity(mActivity -> {
            assertNotNull(mActivity.findViewById(R.id.message));
            text = mActivity.findViewById(R.id.firstNameEdit);
            text.setText("user1");
            String name = text.getText().toString();
            assertNotEquals("user", name);
        });
    }
}

