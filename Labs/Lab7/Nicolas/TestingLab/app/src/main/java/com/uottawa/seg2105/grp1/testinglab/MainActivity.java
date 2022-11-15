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
    private TextView txtMessage;
    private EditText editFirstName, editLastName, editEmail, editPassword;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMessage = findViewById(R.id.txtMessage);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnSubmit = findViewById(R.id.btnSubmit);

    }

    public void authenticateLogin(View view) {
        if (editFirstName.getText().toString().equals("user")) {
            if (editLastName.getText().toString().equals("test")) {
                if (editEmail.getText().toString().equals("email@gmail.com")) {
                    if (editPassword.getText().toString().equals("admin")) {
                        txtMessage.setText("Login Successful");
                    } else {
                        txtMessage.setText("Invalid Password");
                    }
                } else {
                    txtMessage.setText("Invalid Email");
                }
            } else {
                txtMessage.setText("Invalid Last Name");
            }
        }else{
            txtMessage.setText("Invalid First Name");}
        // hide virtual keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}