package com.uottawa.seg2105.grp1.mealer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.lib.Utility;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void btnLoginClient(View view) {

        EditText email = findViewById(R.id.loginEmail);
        EditText password = findViewById(R.id.loginPassword);

        boolean valid = validateCredentials(email, password);

        if(valid) {
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        }
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


}