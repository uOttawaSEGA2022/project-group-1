package com.uottawa.seg2105.grp1.mealer.ui;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uottawa.seg2105.grp1.mealer.R;


public class CookRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("test");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_register);
    }

    public void OnSetAvatarButton(View view) {
        System.out.println("testing");
    }
}