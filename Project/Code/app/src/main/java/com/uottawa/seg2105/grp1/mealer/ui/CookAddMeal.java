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

public class CookAddMeal extends AppCompatActivity {

    //this is for the multiple choice
    TextView cuisineTypeView;
    TextView mealTypeView;
    int selectedCuisine = -1;
    int selectedMeal = -1;
    String[] cuisineTypeArray = {"Italian", "Thai", "Indian", "Other"};
    String[] mealTypeArray = {"Breakfast", "Lunch", "Dinner", "Other"};
    private String cookID;
    User user;
    User cook;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_add_meal);

        cuisineTypeView = findViewById(R.id.cuisineTypeView);
        mealTypeView = findViewById(R.id.mealTypeView);

        //gets cook id
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                cookID = "cook2@email.com";
            } else {
                cookID = extras.getString("cookId");
            }
        } else {
            cookID = (String) savedInstanceState.getSerializable("cookId");
        }

        System.out.println(cookID);
        System.out.println("test");
        System.out.println(cookID);
        //TODO: HERE
        new Thread() {
            @Override
            public void run() {
                try {
                    IRepository rep = MealerSystem.getSystem().getRepository();
                    user = rep.getById(User.class, cookID);
                    User cook = rep.getById(User.class, "cook@email.com");



                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Could not get data from repository.", Toast.LENGTH_LONG).show();
                    });
                }
            }


        }.start();
        //TODO:END
        //System.out.println(arraylistUser.size());
        //System.out.println("Now outside");

        //System.out.println(user);
        //System.out.println(user.getEmail());


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

    public void btnAddItem(View view) {


        EditText menuName = findViewById(R.id.fieldMenuName);
        TextView mealType = findViewById(R.id.mealTypeView);
        TextView cuisineType = findViewById(R.id.cuisineTypeView);
        EditText itemAllergens = findViewById(R.id.fieldAllergens);
        EditText itemIngrediants = findViewById(R.id.fieldIngredients);
        EditText itemDescription = findViewById(R.id.fieldDescription);
        EditText itemPrice = findViewById(R.id.fieldPrice);

        User user = new User();
        //user.getByEmail(cookID);
        //System.out.println(user);
        //System.out.println(user.getEmail());
        //System.out.println(user.getId());



        boolean valid = isValidItem(menuName, mealType, cuisineType, itemAllergens, itemPrice,
                itemIngrediants, itemDescription);
        if (valid) {
            System.out.println(valid);


            view.setEnabled(false);
            new Thread() {
                @Override
                public void run() {
                    try {

                        User cook = MealerSystem.getSystem().getRepository().getById(User.class, cookID);

                        Meal meal = new Meal();
                        meal.createMeal(
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
            //TODO: END HERE
            finish();}
    }
}