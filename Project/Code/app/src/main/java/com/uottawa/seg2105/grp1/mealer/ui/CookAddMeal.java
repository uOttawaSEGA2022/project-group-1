package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.lib.Utility;

public class CookAddMeal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_add_meal);
    }

    private boolean isValidItem(EditText itemName, EditText mealType,
                                  EditText cuisineType, EditText itemAllergens,
                                  EditText itemIngrediants, EditText itemDescription) {
        boolean result = true;


        if (Utility.isEmpty(itemName)) {
            itemName.setError("Item name required");
            result = false;
        }

        if (Utility.isEmpty(mealType)) {
            mealType.setError("Meal type required");
            result = false;
        }


        if (Utility.isEmpty(cuisineType)) {
            cuisineType.setError("Cuisine type required");
            result = false;
        }

        if (Utility.isEmpty(itemAllergens)) {
            itemAllergens.setError("List of allergens required");
            result = false;
        }

        if (Utility.isEmpty(itemIngrediants)) {
            itemIngrediants.setError("List of ingrediants required");
            result = false;
        }

        if (Utility.isEmpty(itemDescription)) {
            itemDescription.setError("Description of item required");
            result = false;
        }

        return result;
    }
    public void btnAddItem(View view){


        EditText menuName = findViewById(R.id.fieldMenuName);
        EditText mealType = findViewById(R.id.fieldMealType);
        EditText cuisineType = findViewById(R.id.fieldCuisineType);
        EditText itemAllergens = findViewById(R.id.fieldAllergens);
        EditText itemIngrediants = findViewById(R.id.fieldIngredients);
        EditText itemDescription = findViewById(R.id.fieldDescription);

        boolean valid = isValidItem(menuName, mealType, cuisineType, itemAllergens,
                 itemIngrediants, itemDescription);
        if (valid) {
            //TODO: add the item to the database (for that cook)
            finish();
        }
    }
}