package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.CookRole;
import com.uottawa.seg2105.grp1.mealer.model.Meal;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.SearchEngine;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    FragmentManager fm;

    private EditText searchNameTx;
    private TextView searchCuisineTx;
    private TextView searchTypeTx;
    private EditText searchMinPriceTx;
    private EditText searchMaxPriceTx;
    private Button searchBtn;

    private ListView searchResultsList;
    private Button backBtn;

    private int selectedCuisine = -1;
    private int selectedType = -1;

    private SpinnerDialog spinner;

    private MealList productsAdapter;

    private String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Cannot interact with window until onStart() finishes successfully
        fm = getSupportFragmentManager();
        spinner = new SpinnerDialog();

        searchNameTx = (EditText) findViewById(R.id.searchMealName);
        searchCuisineTx = (TextView) findViewById(R.id.searchMealCuisine);
        searchTypeTx = (TextView) findViewById(R.id.searchMealType);
        searchMinPriceTx = (EditText) findViewById(R.id.searchMealMinPrice);
        searchMaxPriceTx = (EditText) findViewById(R.id.searchMealMaxPrice);
        searchBtn = (Button) findViewById(R.id.searchSearchBtn);

        searchResultsList = (ListView) findViewById(R.id.searchResultList);
        backBtn = (Button) findViewById(R.id.searchBackBtn);

        productsAdapter = new MealList(SearchActivity.this);
        searchResultsList.setAdapter(productsAdapter);
    }

    public class MealList extends ArrayAdapter<Meal> {
        private Activity context;

        List<Meal> meals;
        public MealList(Activity context) {
            super(context, R.layout.layout_meal_item);
            this.context = context;
            this.meals = new ArrayList<>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_search_result, null, true);

            TextView mealName = (TextView) listViewItem.findViewById(R.id.searchMealListName);
            TextView mealPrice = (TextView) listViewItem.findViewById(R.id.searchMealListPrice);
            Button buyBtn = (Button) listViewItem.findViewById(R.id.searchMealBuyBtn);
            ImageButton viewInfoBtn = (ImageButton) listViewItem.findViewById(R.id.searchMealInfoBtn);

            Meal meal = meals.get(position);
            mealName.setText(meal.getName());

            float price = meal.getPrice();
            mealPrice.setText("$"+String.valueOf(price/100));

            //Just store position where invisible
            buyBtn.setTag(position);
            viewInfoBtn.setTag(position);

            return listViewItem;
        }

        public void update(List<Meal> newMeals) {
            meals.clear();
            meals.addAll(newMeals);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return meals.size();
        }
    }

    public void onNewSearch(View view) {
        if (!isValidQuery())
            return;

        spinner.show(fm, "some_tag");

        new Thread() {
            @Override
            public void run() {

                try {
                    String name = searchNameTx.getText().toString();
                    String cuisine = searchCuisineTx.getText().toString();
                    String type = searchTypeTx.getText().toString();
                    int priceMinCents, priceMaxCents;
                    try {
                        priceMinCents = Math.round(Float.parseFloat(searchMinPriceTx.getText().toString())*100);
                        priceMaxCents = Math.round(Float.parseFloat(searchMaxPriceTx.getText().toString())*100);
                    } catch (NumberFormatException e) {
                        runOnUiThread(() -> {
                            searchMinPriceTx.setError("Please very your prices");
                            searchMaxPriceTx.setError("Please verify your prices");
                            spinner.dismiss();
                        });
                        return;
                    }

                    List<Meal> meals = SearchEngine.searchMeal(name, type, cuisine, priceMinCents, priceMaxCents);

                    runOnUiThread(() -> {
                        if (meals.size() == 0)
                            Toast.makeText(getApplicationContext(), "No meals are available by these criteria.", Toast.LENGTH_SHORT).show();

                        productsAdapter.update(meals);

                        spinner.dismiss();

                    });

                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> {
                        spinner.dismiss();
                        Toast.makeText(getApplicationContext(), "Could not fetch data from repository.", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }.start();
    }

    private boolean isValidQuery() {
        boolean isValid = true;

        if (searchCuisineTx.getText().toString().isEmpty()) {
            searchCuisineTx.setError("Please select a cuisine type.");
            isValid = false;
        }

        if (searchTypeTx.getText().toString().isEmpty()) {
            searchTypeTx.setError("Please select a meal type.");
            isValid = false;
        }

        if (searchMinPriceTx.getText().toString().isEmpty())
            searchMinPriceTx.setText("0.00");

        if (searchMaxPriceTx.getText().toString().isEmpty())
            searchMaxPriceTx.setText("250.00");

        float priceMin = 0;
        float priceMax = 0;

        try {
            priceMin = Float.parseFloat(searchMinPriceTx.getText().toString());
        } catch (NumberFormatException e) {
            searchMinPriceTx.setError("Please enter a valid price");
            isValid = false;
        }

        try {
            priceMax = Float.parseFloat(searchMaxPriceTx.getText().toString());
        } catch (NumberFormatException e) {
            searchMaxPriceTx.setError("Please enter a valid price.");
            isValid = false;
        }

        if (priceMin < 0) {
            searchMinPriceTx.setError("Price cannot be negative.");
            isValid = false;
        }

        if (priceMax < 0) {
            searchMaxPriceTx.setError("Price cannot be negative.");
            isValid = false;
        }

        if (priceMax < priceMin) {
            priceMax = priceMin;
        }

        if (isValid) {
            searchMinPriceTx.setText(String.format("%.2f", priceMin));
            searchMaxPriceTx.setText(String.format("%.2f", priceMax));
        }

        return isValid;
    }

    public void onSelectCuisine(View view) {
        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set title
        builder.setTitle("Select Cuisine");

        // set dialog non cancelable
        builder.setCancelable(false);

        builder.setSingleChoiceItems(Meal.cuisineTypes, selectedCuisine, new DialogInterface.OnClickListener() {
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
                    searchCuisineTx.setText("");
                else
                    searchCuisineTx.setText(Meal.cuisineTypes[selectedCuisine]);
                searchCuisineTx.setError(null);
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

    public void onSelectType(View view) {
        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set title
        builder.setTitle("Select Meal Type");

        // set dialog non cancelable
        builder.setCancelable(false);

        builder.setSingleChoiceItems(Meal.mealTypes, selectedType, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedType = i;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // set text on textView
                if (selectedType == -1)
                    searchTypeTx.setText("");
                else
                    searchTypeTx.setText(Meal.mealTypes[selectedType]);
                searchTypeTx.setError(null);
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

    public void onMealBuy(View view) {
        Intent intent = new Intent(getApplicationContext(), PurchaseActivity.class);
        intent.putExtra("mealId", productsAdapter.meals.get((int)view.getTag()).getId());
        startActivity(intent);
    }

    public void onMealInfo(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_search_info, null);
        dialogBuilder.setView(dialogView);

        Meal meal = productsAdapter.meals.get((int)view.getTag());

        final TextView name = (TextView) dialogView.findViewById(R.id.searchInfoMealName);
        final TextView cuisine = (TextView) dialogView.findViewById(R.id.searchInfoMealCuisine);
        final TextView type = (TextView) dialogView.findViewById(R.id.searchInfoMealType);
        final TextView price  = (TextView) dialogView.findViewById(R.id.searchInfoMealPrice);
        final TextView ingredients = (TextView) dialogView.findViewById(R.id.searchInfoMealIngredients);
        final TextView allergens = (TextView) dialogView.findViewById(R.id.searchInfoMealAllergens);
        final TextView description = (TextView) dialogView.findViewById(R.id.searchInfoMealDescription);

        final TextView cookname = (TextView) dialogView.findViewById(R.id.searchInfoCookName);
        final TextView cookrating = (TextView) dialogView.findViewById(R.id.searchInfoCookRating);

        name.setText(meal.getName());
        cuisine.setText(meal.getCuisine());
        type.setText(meal.getType());
        float floatPrice = meal.getPrice();
        price.setText("$"+floatPrice/100);
        ingredients.setText(meal.getIngredients());
        allergens.setText(meal.getAllergens());
        description.setText(meal.getDescription());

        cookname.setText(meal.getCook().getFirstName() + " " + meal.getCook().getLastName());
        CookRole cookRole = (CookRole) meal.getCook().getRole();
        cookrating.setText(String.format("%.1f", cookRole.getAverageRating()));

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void onCancel(View view) { finish(); }
}