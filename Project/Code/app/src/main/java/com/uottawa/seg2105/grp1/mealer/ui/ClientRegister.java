package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.lib.Utility;
import com.uottawa.seg2105.grp1.mealer.model.ClientRole;
import com.uottawa.seg2105.grp1.mealer.model.CookRole;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.User;
import com.uottawa.seg2105.grp1.mealer.model.UserRole;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import okhttp3.internal.Util;

public class ClientRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);
    }

    private boolean isValidClient(EditText firstName, EditText lastName,
                                  EditText email, EditText password,
                                  EditText address,
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
                lastName.setError("Last Name invalid");
                result = false;
            }
        }

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

        if(Utility.isEmpty(address)) {
            address.setError("Address required");
            result = false;
        } else {
            if (!Utility.isValidField(address, Utility.ADDRESS)) {
                address.setError("Address invalid");
                result = false;
            }
        }

        if (Utility.isEmpty(ccNumber)) {
            ccNumber.setError("Credit Card Number required");
            result = false;
        } else {
            if (!Utility.isValidField(ccNumber, Utility.CREDITCARD)) {
                ccNumber.setError("Credit Card Number invalid");
                result = false;
            }
        }

        if(Utility.isEmpty(ccExpiry)) {
            ccExpiry.setError("Credit Card Expiry required");
            result = false;
        } else {
            if (!Utility.isValidField(ccExpiry, Utility.CREDITCARDEXPIRY)) {
                ccExpiry.setError("Credit Card Expiry invalid");
                result = false;
            }
        }
        return result;
    }

    public void onRegisterClient(View view) throws InterruptedException {
        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText address = findViewById(R.id.address);
        EditText ccNumber = findViewById(R.id.ccNumber);
        EditText ccExpiry = findViewById(R.id.ccExpiry);

        boolean valid = isValidClient(firstName, lastName, email, password,
                                      address, ccNumber, ccExpiry);

        if (valid) {
            // TODO: Use User.createNewUser() when it is completed
            try {
                view.setEnabled(false);
                ClientRole role = new ClientRole();
                User newUser = User.createNewUser(
                        firstName.getText().toString(), lastName.getText().toString(),
                        email.getText().toString(), password.getText().toString(),
                        address.getText().toString(), role, false);
                role.setCardNumber(ccNumber.getText().toString());
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        boolean success = MealerSystem.getSystem().tryLogin(
                                email.getText().toString(),
                                password.getText().toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (success) {
                                    Toast.makeText(ClientRegister.this, "Login successful!", Toast.LENGTH_LONG).show();
                                    finish(); // Return to LoginActivity (main) so it redirects to the correct home page.
                                } else {
                                    Toast.makeText(ClientRegister.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                                    view.setEnabled(true);
                                }
                            }
                        });
                    }
                };
                t.start();
            } catch (RepositoryRequestException e) {
                // TODO: Add a UserAlreadyExistsException
                Toast.makeText(this, "An error occured", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }
}