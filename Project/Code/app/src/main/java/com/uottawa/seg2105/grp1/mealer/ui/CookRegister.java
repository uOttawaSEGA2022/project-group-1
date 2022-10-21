package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.lib.Utility;
import com.uottawa.seg2105.grp1.mealer.model.CookRole;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.User;
import com.uottawa.seg2105.grp1.mealer.model.UserAlreadyExistsException;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

public class CookRegister extends AppCompatActivity {
    boolean check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_register);
    }

    //opens the VoidCheck creator
    public void btnVoidCheck(View view){
        Intent intent = new Intent(getApplicationContext(), ImageCheck.class);
        startActivityForResult(intent, 5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) return;
        //if it returns a "custom" string, then change the logo to the image
        if (data.getStringExtra("RESULT_STRING") != null) {
            //Use Data to get "custom" string that says the locaiton of photo
            String custom = data.getStringExtra("RESULT_STRING");

            //sets the image as the custom photo
            ImageView avatarImage = (ImageView) findViewById(R.id.imageView);
            avatarImage.setImageBitmap(BitmapFactory.decodeFile(custom));
            check = true;
        }
    }

    private boolean isValidClient(EditText firstName, EditText lastName,
                                  EditText email, EditText password,
                                  EditText address1) {
        boolean result = true;

        //if no image was added, cause a problem
        if(!check){
            result = false;
        }

        if(Utility.isEmpty(firstName)) {
            firstName.setError("First Name required");
            result = false;
        } else {
            //If name is not valid, say it's invalid and set result false
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

        if(Utility.isEmpty(address1)) {
            address1.setError("Address required");
            result = false;
        } else {
            if (!Utility.isValidField(address1, Utility.ADDRESS)) {
                address1.setError("Address invalid");
                result = false;
            }
        }

        return result;
    }//end of confirmation

    public void btnRegisterClient(View view) {
        EditText firstName = findViewById(R.id.fieldName);
        EditText lastName = findViewById(R.id.fieldLastName);
        EditText email = findViewById(R.id.fieldEmail);
        EditText password = findViewById(R.id.fieldPassword);
        EditText address1 = findViewById(R.id.fieldAddress);

        boolean valid = isValidClient(firstName, lastName, email, password, address1);

        if (!check){
            Toast.makeText(this, "You forgot to add a Check", Toast.LENGTH_LONG).show();
            return;
        }

        if(valid) {
            view.setEnabled(false);
            new Thread() {
                @Override
                public void run() {
                    try {
                        CookRole role = new CookRole();
                        User.createNewUser(
                                firstName.getText().toString(), lastName.getText().toString(),
                                email.getText().toString(), password.getText().toString(),
                                address1.getText().toString(), role, false);

                        boolean success = MealerSystem.getSystem().tryLogin(
                                email.getText().toString(),
                                password.getText().toString());

                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(CookRegister.this, "Registering successful!", Toast.LENGTH_LONG).show();
                                finish(); // Return to LoginActivity (main) so it redirects to the correct home page.
                            } else {
                                Toast.makeText(CookRegister.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                view.setEnabled(true);
                            }
                        });
                    } catch (RepositoryRequestException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(CookRegister.this, "An error occurred.", Toast.LENGTH_LONG).show();
                            view.setEnabled(true);
                        });

                    } catch (UserAlreadyExistsException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(CookRegister.this, "This email is already taken.", Toast.LENGTH_LONG).show();
                            view.setEnabled(true);
                        });
                    }
                }
            }.start();
        }
    }


}