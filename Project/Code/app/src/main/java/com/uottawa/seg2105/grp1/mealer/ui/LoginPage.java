package com.uottawa.seg2105.grp1.mealer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.lib.Utility;
import com.uottawa.seg2105.grp1.mealer.model.System;

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

    public void btnLoginClient(View view) {

        EditText email = findViewById(R.id.loginEmail);
        EditText password = findViewById(R.id.loginPassword);

        boolean valid = validateCredentials(email, password);

        if(valid) {
            /*
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();*/
            boolean success = System.getSystem().tryLogin(email.getText().toString(),
                                        password.getText().toString());

            if (success) {
                Intent intent = new Intent(getApplicationContext(), ClientHome.class);
                startActivityForResult(intent, 0);
            }
        }
    }

}