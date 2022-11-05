package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.lib.Utility;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;

public class SuspensionActivity extends AppCompatActivity {

    //Set public & static for datePick fragment to edit
    @SuppressLint("StaticFieldLeak")
    public static TextView banDateText;

    private EditText banReason;
    private ToggleButton banPermaToggle;
    private CheckBox banConfirmBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspension);

        banDateText = (TextView) findViewById(R.id.banDateText);
        banReason = (EditText) findViewById(R.id.banReason);
        banPermaToggle = (ToggleButton) findViewById(R.id.banPermaToggle);
        banConfirmBox = (CheckBox) findViewById(R.id.banConfirmBox);
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        //getSupportFragmentManager().beginTransaction().replace(R.id.container_a, newFragment).commit();
    }

    public void banBtn(View view) {
        validateBanForm();
    }


    private boolean validateBanForm() {

        boolean result = true;

        //Cases for banReason: empty & < 10 chars
        if(Utility.isEmpty(banReason)) {
            banReason.setError("Ban reason required");
            result = false;
        } else if (banReason.getText().toString().length() < 10) {
            banReason.setError("Include at least 10 characters");
            result = false;
        }

        //Case where the ban is not permanent and no date included
        if (banDateText.getText().toString().charAt(0) == '('
         && banPermaToggle.getText().toString().equals("NO")) {
            banDateText.setError("Not a permaban; choose a date");
            result = false;
        }

        //Check that confirmation box is checked
        if (!banConfirmBox.isChecked()) {
            banConfirmBox.setError("Confirmation required");
            result = false;
        }

        return result;
    }

    //If ban toggle is 'YES', banDate's text changes accordingly
    //to guide the user
    public void banToggleBtn(View view) {
        if (banPermaToggle.getText().toString().equals("NO")) {
            banDateText.setText("(Pick a date)");
        } else {
            banDateText.setText("(Not applicable)");
        }
    }

    public void banCheckBoxBtn(View view) {
        if (!banConfirmBox.isChecked()) {
            banConfirmBox.setText("Confirm Ban");
        } else {
            banConfirmBox.setText("Ban Confirmed");
        }
    }

/*
    public void btnBan(View view) {

        EditText banReason = findViewById(R.id.banReason);
        EditText banDate = findViewById(R.id.banDate);
        //R.id.banCookName
        //R.id.banCookEmail
        R.id.banPermaToggle

        boolean valid = validateBanForm(email, password);

        if(valid) {
            view.setEnabled(false);

            new Thread() {
                @Override
                public void run() {
                    boolean success = MealerSystem.getSystem().tryLogin(
                            email.getText().toString(),
                            password.getText().toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (success) {
                                Toast.makeText(LoginPage.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                finish(); // Return to LoginActivity (main) so it redirects to the correct home page.
                            } else {
                                Toast.makeText(LoginPage.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                                view.setEnabled(true);
                            }
                        }
                    });
                }
            }.start();
        }*/

}