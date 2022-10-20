package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.text.TextUtils;
import android.widget.EditText;

import com.uottawa.seg2105.grp1.mealer.R;

public class ClientRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);
    }

    private boolean isEmpty(EditText editText) {
        String str = editText.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private boolean isValidClient(EditText firstName, EditText lastName,
                                  EditText email, EditText password,
                                  EditText address1, EditText address2,
                                  EditText ccNumber, EditText ccExpiry) {
        boolean result = true;
        boolean hasAddress2 = true;
        if(isEmpty(firstName)) {
            firstName.setError("First Name required");
            result = false;
        }
        if (isEmpty(lastName)) {
            lastName.setError("Last Name required");
            result = false;
        }
        if(isEmpty(email)) {
            email.setError("Email required");
            result = false;
        }
        if (isEmpty(password)) {
            password.setError("Password required");
            result = false;
        }
        if(isEmpty(address1)) {
            address1.setError("Address required");
            result = false;
        }
        // address2 is optional, just check if it is present
        if(isEmpty(address2)) {
            hasAddress2 = false;
        }
        if (isEmpty(ccNumber)) {
            ccNumber.setError("Credit Card Number required");
            result = false;
        }
        if(isEmpty(ccExpiry)) {
            ccExpiry.setError("Credit Card Expiry required");
            result = false;
        }
        return result;
    }

    public void onRegisterClient(View view) {
        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText address1 = findViewById(R.id.address1);
        EditText address2 = findViewById(R.id.address2);
        EditText ccNumber = findViewById(R.id.ccNumber);
        EditText ccExpiry = findViewById(R.id.ccExpiry);
        boolean valid = isValidClient(firstName, lastName, email, password,
                                      address1, address2, ccNumber, ccExpiry);

        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}