package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uottawa.seg2105.grp1.mealer.R;

public class DebugMode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_mode);
    }


    public void btnOpenCook(View view){
        System.out.println("debug");
        Intent intent = new Intent(getApplicationContext(), CookAddMeal.class);
        startActivity(intent);
    }

    public void btnOpenComplaint(View view){
        System.out.println("debug");
        Intent intent = new Intent(getApplicationContext(), ComplaintActivity.class);
        startActivity(intent);
    }

}