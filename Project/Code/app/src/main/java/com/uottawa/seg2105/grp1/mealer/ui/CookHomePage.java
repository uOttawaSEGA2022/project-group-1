package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.User;
import com.uottawa.seg2105.grp1.mealer.model.CookRole;

public class CookHomePage extends AppCompatActivity {

    private TextView welcomeTxt;
    private TextView mealsSold;
    private TextView ratingTxt;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_home_page);

        User cook = MealerSystem.getSystem().getCurrentUser();
        String fullname = cook.getFirstName() + " " + cook.getLastName();

        welcomeTxt = (TextView) findViewById(R.id.welcomeTxt);
        welcomeTxt.setText("Welcome" + fullname);

        CookRole role = (CookRole) cook.getRole();
        String rating_string = String.format("%.1f", role.getAverageRating());

        ratingTxt = (TextView) findViewById(R.id.ratingTxt);
        ratingTxt.setText("Rating: " + rating_string);

        long mealsSoldNum = role.getMealsSold();

        mealsSold = (TextView) findViewById(R.id.mealsSold);
        mealsSold.setText("Meals Sold: " + mealsSoldNum);
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