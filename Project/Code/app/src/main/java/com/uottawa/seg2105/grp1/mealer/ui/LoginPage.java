package com.uottawa.seg2105.grp1.mealer.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.lib.Utility;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    private boolean validateCredentials(EditText email, EditText password) {

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

    public void btnLogin(View view) {

        EditText email = findViewById(R.id.loginEmail);
        EditText password = findViewById(R.id.loginPassword);

        boolean valid = validateCredentials(email, password);

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
        }
    }

}