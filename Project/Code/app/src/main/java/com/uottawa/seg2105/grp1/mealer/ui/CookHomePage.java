package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.Complaint;
import com.uottawa.seg2105.grp1.mealer.model.User;

public class CookHomePage extends AppCompatActivity {

    private TextView welcomeTxt;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_home_page);

        welcomeTxt = (TextView) findViewById(R.id.welcomeTxt);
        welcomeTxt.setText("Welcome" + );
    }

    public void btnMealList(View view){
        System.out.println("mealList");
        Intent intent = new Intent(getApplicationContext(), MealListActivity.class);
        startActivity(intent);
    }

    public void btnSettingsMngmnt(View view){
        System.out.println("settingsMngmnt");
        Intent intent = new Intent(getApplicationContext(), CookSettingsActivity.class);
        startActivity(intent);
    }
}