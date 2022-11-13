package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.lib.Utility;
import com.uottawa.seg2105.grp1.mealer.model.ClientRole;
import com.uottawa.seg2105.grp1.mealer.model.Complaint;
import com.uottawa.seg2105.grp1.mealer.model.Meal;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.User;
import com.uottawa.seg2105.grp1.mealer.model.UserAlreadyExistsException;
import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CookAddMeal extends AppCompatActivity {

    //this is for the multiple choice
    TextView cuisineTypeView;
    TextView mealTypeView;
    int selectedCuisine = -1;
    int selectedMeal = -1;
    String[] cuisineTypeArray = {"Italian", "Thai", "Indian", "Other"};
    String[] mealTypeArray = {"Breakfast", "Lunch", "Dinner", "Other"};
    private String cookID = null;
    private String mealID = null;
    User user;
    User cook;
    private List<Meal> meals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_add_meal);

        cuisineTypeView = findViewById(R.id.cuisineTypeView);
        mealTypeView = findViewById(R.id.mealTypeView);

        //gets cook id and meal id
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                mealID = null;
                cookID = "cook@email.com";
            } else {
                cookID = extras.getString("cookId");
                if (getIntent().hasExtra("mealId")) {
                    if(!extras.getString("mealId").equals(null)){
                        mealID = extras.getString("mealId");}}

            }
        } else {
            cookID = (String) savedInstanceState.getSerializable("mealId");
        }

        //checks if its being updated or created
        if (mealID != null){
            EditText menuName = findViewById(R.id.fieldMenuName);
            TextView mealType = findViewById(R.id.mealTypeView);
            TextView cuisineType = findViewById(R.id.cuisineTypeView);
            EditText itemAllergens = findViewById(R.id.fieldAllergens);
            EditText itemIngrediants = findViewById(R.id.fieldIngredients);
            EditText itemDescription = findViewById(R.id.fieldDescription);
            EditText itemPrice = findViewById(R.id.fieldPrice);
            Button updateBtn = findViewById(R.id.btnAddItem);

            new Thread() {
                @Override
                public void run() {
                    try {

                        Meal meal = MealerSystem.getSystem().getRepository().getById(Meal.class, mealID);
                        System.out.println(meal.getDescription());
                        System.out.println(meal.getAllergens());
                        System.out.println(meal.getName());

                        runOnUiThread(() -> {
                            menuName.setText(meal.getName());
                            mealType.setText(meal.getType());
                            cuisineType.setText(meal.getCuisine());
                            itemAllergens.setText(meal.getAllergens());
                            itemIngrediants.setText(meal.getIngredients());
                            itemDescription.setText(meal.getDescription());
                            updateBtn.setText("Update the item");
                        });

                    } catch (RepositoryRequestException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(CookAddMeal.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                        });

                    }
                }
            }.start();
        }




        //Cuisine multiple choice
        cuisineTypeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(CookAddMeal.this);

                // set title
                builder.setTitle("Select Cuisine Type");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setSingleChoiceItems(cuisineTypeArray, selectedCuisine, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedCuisine = i;
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // set text on textView
                        if (selectedCuisine == -1)
                            cuisineTypeView.setText("");
                        else
                            cuisineTypeView.setText(cuisineTypeArray[selectedCuisine]);
                        //cuisineTypeView.setText("test");
                        System.out.println("okkakyyy");
                    }
                });

                //put Cancel on the left side (Neutral)
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });

                // show dialog
                builder.show();
            }
        });


        //Meal Type multiple choice
        mealTypeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(CookAddMeal.this);

                // set title
                builder.setTitle("Select Meal Type");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setSingleChoiceItems(mealTypeArray, selectedMeal, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedMeal = i;
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // set text on textView
                        if (selectedMeal == -1)
                            mealTypeView.setText("");
                        else
                            mealTypeView.setText(mealTypeArray[selectedMeal]);
                        //cuisineTypeView.setText("test");
                        System.out.println("okkakyyy");
                    }
                });

                //put Cancel on the left side (Neutral)
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });

                // show dialog
                builder.show();
            }
        });
    }
    //end of multiple choice


    private boolean isValidItem(EditText itemName, TextView mealType, TextView cuisineType,
                                EditText itemAllergens, EditText itemPrice,
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

        if (Utility.isEmpty(itemPrice)) {
            itemPrice.setError("List of allergens required");
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

    public void btnAddItem(View view) throws InterruptedException {


        EditText menuName = findViewById(R.id.fieldMenuName);
        TextView mealType = findViewById(R.id.mealTypeView);
        TextView cuisineType = findViewById(R.id.cuisineTypeView);
        EditText itemAllergens = findViewById(R.id.fieldAllergens);
        EditText itemIngrediants = findViewById(R.id.fieldIngredients);
        EditText itemDescription = findViewById(R.id.fieldDescription);
        EditText itemPrice = findViewById(R.id.fieldPrice);

        boolean valid = isValidItem(menuName, mealType, cuisineType, itemAllergens, itemPrice,
                itemIngrediants, itemDescription);
        if (valid) {
            if (mealID == null) { //if we have no mealID, create a new item

                view.setEnabled(false);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            User cook = MealerSystem.getSystem().getRepository().getById(User.class, cookID);
                            Meal meal = new Meal();
                            meal.createMeal( //create a meal with all the atributes
                                    menuName.getText().toString(), mealType.getText().toString(),
                                    cuisineType.getText().toString(), itemIngrediants.getText().toString(),
                                    itemAllergens.getText().toString(), Integer.parseInt(itemPrice.getText().toString()),
                                    itemDescription.getText().toString(), cook, true);

                        } catch (RepositoryRequestException e) {
                            runOnUiThread(() -> {
                                Toast.makeText(CookAddMeal.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                                view.setEnabled(true);
                            });

                        }
                    }
                }.start();
                TimeUnit.SECONDS.sleep(1); //This is here, so it gives time to update before closing
                finish();
            }

        //if we have a mealID, update
        else {
                view.setEnabled(false);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            User cook = MealerSystem.getSystem().getRepository().getById(User.class, cookID);
                            Meal meal = MealerSystem.getSystem().getRepository().getById(Meal.class, mealID);
                            meal.updateMeal( //create a meal with all the attributes
                                   menuName.getText().toString(), mealType.getText().toString(),
                                   cuisineType.getText().toString(), itemIngrediants.getText().toString(),
                                   itemAllergens.getText().toString(), Integer.parseInt(itemPrice.getText().toString()),
                                   itemDescription.getText().toString(), cook, true);
                        } catch (RepositoryRequestException e) {
                            runOnUiThread(() -> {
                                Toast.makeText(CookAddMeal.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                                view.setEnabled(true);
                            });
                        }
                    }
                }.start();
                TimeUnit.SECONDS.sleep(1); //This is here, so it gives time to update before closing
                finish();
            }
        }
    }
}