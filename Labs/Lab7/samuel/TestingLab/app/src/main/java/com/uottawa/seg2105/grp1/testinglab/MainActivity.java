package com.uottawa.seg2105.grp1.testinglab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView message;
    private EditText firstname, lastname, email, password;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = findViewById(R.id.message);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);

    }

    public void authenticateLogin(View view) {
        if (firstname.getText().toString().equals("user"))
            if (lastname.getText().toString().equals("test"))
                if (email.getText().toString().equals("email@gmail.com"))
                    if (password.getText().toString().equals("admin"))
                        message.setText("Login Successful");
                    else
                        message.setText("Invalid Password");
                else
                    message.setText("Invalid Email");
            else
                message.setText("Invalid Last Name");
        else
            message.setText("Invalid First Name");
        // hide virtual keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}