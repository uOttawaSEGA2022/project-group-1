package com.example.testinglab;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText username;
    private EditText lastname;
    private EditText EmailID;
    private EditText password;
    private TextView message;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupVariables();
    }
    public void authenticateLogin(View view) {
        if (username.getText().toString().equalsIgnoreCase("user"))
            if (lastname.getText().toString().equalsIgnoreCase("test"))
                if (EmailID.getText().toString().equalsIgnoreCase("email@gmail.com"))
                    if (password.getText().toString().equalsIgnoreCase("admin"))
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
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
    private void setupVariables() {
        username = (EditText) findViewById(R.id.firstNameEdit);
        lastname = (EditText)findViewById(R.id.lastNameEdit) ;
        EmailID = (EditText)findViewById(R.id.emailEdit) ;
        password = (EditText) findViewById(R.id.passwordEdit);
        login = (Button) findViewById(R.id.submitBtn);
        message = (TextView) findViewById(R.id.message);

    }
}