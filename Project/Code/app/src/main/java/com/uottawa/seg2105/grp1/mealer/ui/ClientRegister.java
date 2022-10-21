package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.text.TextUtils;
import android.widget.EditText;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.lib.Utility;

import okhttp3.internal.Util;

public class ClientRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);
    }

    private boolean isValidClient(EditText firstName, EditText lastName,
                                  EditText email, EditText password,
                                  EditText address1, EditText address2,
                                  EditText ccNumber, EditText ccExpiry) {
        boolean result = true;
        boolean hasAddress2 = true;

        if(Utility.isEmpty(firstName)) {
            firstName.setError("First Name required");
            result = false;
        } else {
            //If non-empty, check for validity
            if (!Utility.isValidField(firstName, Utility.NAME)) {
                firstName.setError("First Name invalid");
                result = false;
            }
        }

        if (Utility.isEmpty(lastName)) {
            lastName.setError("Last Name required");
            result = false;
        } else {
            if (!Utility.isValidField(lastName, Utility.NAME)) {
                firstName.setError("Last Name invalid");
                result = false;
            }
        }

        if(Utility.isEmpty(email)) {
            email.setError("Email required");
            result = false;
        } else {
            if (!Utility.isValidField(email, Utility.EMAIL)) {
                firstName.setError("Email invalid");
                result = false;
            }
        }

        if (Utility.isEmpty(password)) {
            password.setError("Password required");
            result = false;
        }

        if(Utility.isEmpty(address1)) {
            address1.setError("Address required");
            result = false;
        } else {
            if (!Utility.isValidField(address1, Utility.ADDRESS)) {
                firstName.setError("Address invalid");
                result = false;
            }
        }

        // address2 is optional, just check if it is present
        if(Utility.isEmpty(address2)) {
            hasAddress2 = false;
        } else {
            if (!Utility.isValidField(address2, Utility.ADDRESS)) {
                firstName.setError("Address invalid");
                result = false;
            }
        }

        if (Utility.isEmpty(ccNumber)) {
            ccNumber.setError("Credit Card Number required");
            result = false;
        } else {
            if (!Utility.isValidField(ccNumber, Utility.CREDITCARD)) {
                firstName.setError("Credit Card Number invalid");
                result = false;
            }
        }

        if(Utility.isEmpty(ccExpiry)) {
            ccExpiry.setError("Credit Card Expiry required");
            result = false;
        } else {
            if (!Utility.isValidField(ccExpiry, Utility.CREDITCARDEXPIRY)) {
                firstName.setError("Credit Card Expiry invalid");
                result = false;
            }
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

        if (valid) {
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}