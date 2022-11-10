package com.example.lab7testing;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);
    private MainActivity mActivity = null;
    private TextView text;

    //@Before
    //public void setUp() throws Exception {
    //    mActivity = ActivityScenario.setupActivity(MainActivity.class);
    // }

    @Test
    public void checkFirstName() throws Exception {
        ActivityScenario<MainActivity> scenario = rule.getScenario();
        scenario.onActivity(activity -> {
            text = activity.findViewById(R.id.username);
            text.setText("user1");
            String name = text.getText().toString();
            assertNotEquals("user", name);
        });
    }

    @Test
    public void checkLastName() throws Exception {
        ActivityScenario<MainActivity> scenario = rule.getScenario();
        scenario.onActivity(activity -> {
            text = activity.findViewById(R.id.username);
            text.setText("user");
            text = activity.findViewById(R.id.Lastname);
            text.setText("test1");
            String name = text.getText().toString();
            assertNotEquals("test", name);
        });
    }

    @Test
    public void checkPassword() throws Exception {
        ActivityScenario<MainActivity> scenario = rule.getScenario();
        scenario.onActivity(activity -> {
            text = activity.findViewById(R.id.username);
            text.setText("user");
            text = activity.findViewById(R.id.Lastname);
            text.setText("test");
            text = activity.findViewById(R.id.password);
            text.setText("admin1");
            String pass = text.getText().toString();
            assertNotEquals("admin", pass);
        });
    }
}
