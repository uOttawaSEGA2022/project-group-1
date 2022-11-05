package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.lib.Utility;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;

public class SuspensionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspension);
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private boolean validateBanForm(EditText email, EditText password) {

        boolean result = true;

        if(Utility.isEmpty(email)) {
            email.setError("Email required");
            result = false;
        } else {
            if (!Utility.isValidField(email, Utility.EMAIL)) {
                email.setError("Email invalid");
                result = false;
            }
        }

        if (Utility.isEmpty(password)) {
            password.setError("Password required");
            result = false;
        }

        return result;
    }

    public void btnBan(View view) {
/*
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
}