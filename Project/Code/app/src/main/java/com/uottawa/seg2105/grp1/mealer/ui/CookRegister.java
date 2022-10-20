package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.R;

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

    //add here for confirmation
    private boolean isEmpty(EditText editText) {
        String str = editText.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private boolean isValidClient(EditText firstName, EditText lastName,
                                  EditText email, EditText password,
                                  EditText address1) {
        boolean result = true;

        //if no image was added, cause a problem
        if(check != true){
            result = false;
        }

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

        return result;
    }//end of confirmation

    public void btnRegisterClient(View view) {
        EditText firstName = findViewById(R.id.fieldName);
        EditText lastName = findViewById(R.id.fieldLastName);
        EditText email = findViewById(R.id.fieldEmail);
        EditText password = findViewById(R.id.fieldPassword);
        EditText address1 = findViewById(R.id.fieldAddress);

        boolean valid = isValidClient(firstName, lastName, email, password,
                address1);

        if (check != true){
            Toast.makeText(this, "You forgot to add a Check", Toast.LENGTH_LONG).show();
        }

        if(valid) {
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }


}