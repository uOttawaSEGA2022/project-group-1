package com.uottawa.seg2105.grp1.testinglab;

import static org.junit.Assert.*;

import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);
    private TextView text;

    @Test
    public void checkFirstName() throws Exception {
        mActivityScenarioRule.getScenario().onActivity(activity -> {
            text = activity.findViewById(R.id.editFirstName);
            text.setText("user1");

            String name = text.getText().toString();
            assertNotEquals("user", name);
        });
    }

    @Test
    public void checkLastName() throws Exception {
        mActivityScenarioRule.getScenario().onActivity(activity -> {
            text = activity.findViewById(R.id.editFirstName);
            text.setText("user");
            text = activity.findViewById(R.id.editLastName);
            text.setText("test1");

            String name = text.getText().toString();
            assertNotEquals("test", name);
        });
    }

    @Test
    public void checkEmail() throws Exception {
        mActivityScenarioRule.getScenario().onActivity(activity -> {
            text = activity.findViewById(R.id.editFirstName);
            text.setText("user");
            text = activity.findViewById(R.id.editLastName);
            text.setText("test");
            text = activity.findViewById(R.id.editEmail);
            text.setText("email@gmail.com1");

            String email = text.getText().toString();
            assertNotEquals("email@gmail.com", email);
        });
    }

    @Test
    public void checkPassword() throws Exception {
        mActivityScenarioRule.getScenario().onActivity(activity -> {
            text = activity.findViewById(R.id.editFirstName);
            text.setText("user");
            text = activity.findViewById(R.id.editLastName);
            text.setText("test");
            text = activity.findViewById(R.id.editEmail);
            text.setText("email@gmail.com");
            text = activity.findViewById(R.id.editLastName);
            text.setText("admin1");

            String pass = text.getText().toString();
            assertNotEquals("admin", pass);
        });
    }
}
