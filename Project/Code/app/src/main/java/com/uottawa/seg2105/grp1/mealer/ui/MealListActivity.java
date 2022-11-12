package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.Meal;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MealListActivity extends AppCompatActivity {

    private String cookID;

    private ListView listViewMeals;
    private Button createMealBtn;

    private SpinnerDialog spinner;

    MealList productsAdapter;
    private List<Meal> meals;

    private String TAG = "MealListActivity";

    public class MealList extends ArrayAdapter<Meal> {
        private Activity context;

        List<Meal> meals;
        TextView mealName, mealPrice;
        CheckBox mealInStock;
        ImageButton removeMealBtn;


        public MealList(Activity context, List<Meal> meals) {
            super(context, R.layout.layout_meal_list, meals);
            this.context = context;
            this.meals = meals;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_meal_list, null, true);

            mealName = (TextView) listViewItem.findViewById(R.id.mealListName);
            mealPrice = (TextView) listViewItem.findViewById(R.id.mealListPrice);
            mealInStock = (CheckBox) listViewItem.findViewById(R.id.mealInStockCheck);
            removeMealBtn = (ImageButton) listViewItem.findViewById(R.id.imageButton);

            Meal meal = meals.get(position);
            mealName.setText(meal.getName());
            mealPrice.setText("$"+String.valueOf(meal.getPrice()));
            mealInStock.setChecked(meal.getIsOffered());

            //Just store position where invisible
            mealName.setTag(position);
            mealInStock.setTag(position);
            removeMealBtn.setTag(position);

            mealName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = (int) mealName.getTag();
                    showMealDetailsDialog(meals.get(position));
                    return true;
                }
            });
            return listViewItem;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);

        //Cannot interact with window until onStart() finishes successfully
        FragmentManager fm = getSupportFragmentManager();
        spinner = new SpinnerDialog();
        spinner.show(fm, "some_tag");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                cookID = null;
            } else {
                cookID = extras.getString("cookId");
            }
        } else {
            cookID = (String) savedInstanceState.getSerializable("cookId");
        }

        listViewMeals = (ListView) findViewById(R.id.listViewMeals);
        createMealBtn = (Button) findViewById(R.id.createMealBtn);

        meals = new ArrayList<>();

        /*listViewMeals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal meal = meals.get(i);
                showMealDetailsDialog(meals.get(i));
                return true;
            }
        });*/
    }


    @Override
    protected void onStart() {
        super.onStart();

        new Thread() {
            @Override
            public void run() {

                try {
                    meals = MealerSystem.getSystem().getRepository().query(
                            Meal.class,
                            (m) -> m.getCook().getId().equals(cookID)
                    );

                    runOnUiThread(() -> {
                        productsAdapter = new MealList(MealListActivity.this, meals);
                        listViewMeals.setAdapter(productsAdapter);

                        //Window interactable once meals are loaded up
                        spinner.dismiss();

                    });

                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Could not fetch data from repository.", Toast.LENGTH_LONG).show());
                }
            }
        }.start();

    }

    public void createNewMealBtn(View view) {
        Intent intent = new Intent(getApplicationContext(), MealCreateActivity.class);
        intent.putExtra("cookId", cookID);
        startActivity(intent);
    }

    public void toggleInStock(View view) {

        int position = (int) view.findViewById(R.id.mealInStockCheck).getTag();
        Log.d(TAG, "meal position selected (stock): "+position);

        //Block user from interacting (like spamming the check box)
        FragmentManager fm = getSupportFragmentManager();
        spinner = new SpinnerDialog();
        spinner.show(fm, "some_tag");

        new Thread() {
            @Override
            public void run() {

                try {
                    Meal meal = meals.get(position);
                    boolean isOffered = meal.getIsOffered();
                    Map<String, Object> properties = meal.serialise();

                    properties.replace("isOffered", !isOffered);//toggle isOffered

                    MealerSystem.getSystem().getRepository().update(Meal.class, meal.getId(), properties);

                    Meal updatedMeal = MealerSystem.getSystem().getRepository().getById(Meal.class, meal.getId());
                    meals.set(position, updatedMeal);

                    runOnUiThread(() -> {
                        spinner.dismiss();
                    });

                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Could not fetch data from repository.", Toast.LENGTH_LONG).show());
                }
            }
        }.start();
    }

    private void showMealDetailsDialog(Meal meal) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.meal_details_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView name = (TextView) dialogView.findViewById(R.id.mealDtlsName);
        final TextView cuisine = (TextView) dialogView.findViewById(R.id.mealDtlsCuisine);
        final TextView type = (TextView) dialogView.findViewById(R.id.mealDtlsType);
        final TextView price  = (TextView) dialogView.findViewById(R.id.mealDtlsPrice);
        final TextView ingredients = (TextView) dialogView.findViewById(R.id.mealDtlsIngredients);
        final TextView allergens = (TextView) dialogView.findViewById(R.id.mealDtlsAllergens);
        final TextView description = (TextView) dialogView.findViewById(R.id.mealDtlsDesc);

        final Button editBtn = (Button) dialogView.findViewById(R.id.updateMealBtn);
        final Button cancelBtn = (Button) dialogView.findViewById(R.id.cancelMealDialogBtn);

        name.setText(meal.getName());
        cuisine.setText(meal.getCuisine());
        type.setText(meal.getType());
        price.setText("$"+meal.getPrice());
        ingredients.setText(meal.getIngredients());
        allergens.setText(meal.getAllergens());
        description.setText(meal.getDescription());

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealCreateActivity.class);
                intent.putExtra("cookId", cookID);
                intent.putExtra("mealId", meal.getId());
                startActivity(intent);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void removeMeal(View view) {

        int position = (int) view.findViewById(R.id.imageButton).getTag();
        Log.d(TAG, "meal position selected (removeBtn): "+position);
        showRemoveMealDialog(position);
    }

    private void showRemoveMealDialog(int mealPosition) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_meal_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView delMealName = (TextView) dialogView.findViewById(R.id.deleteMealName);
        final TextView delMealPrice  = (TextView) dialogView.findViewById(R.id.deleteMealPrice);
        final Button removeConfirm = (Button) dialogView.findViewById(R.id.delMealBtn);

        delMealName.setText(meals.get(mealPosition).getName());
        delMealPrice.setText("$"+meals.get(mealPosition).getPrice());

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        removeConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "Removing meal with ID: "+meals.get(mealPosition).getId());

                FragmentManager fm = getSupportFragmentManager();
                spinner = new SpinnerDialog();
                spinner.show(fm, "some_tag");

                new Thread() {
                    @Override
                    public void run() {

                        try {
                            MealerSystem.getSystem().getRepository().delete(
                                    Meal.class, meals.get(mealPosition)
                            );

                            runOnUiThread(() -> {
                                productsAdapter.remove(meals.get(mealPosition));
                                productsAdapter.notifyDataSetChanged();

                                spinner.dismiss();
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Meal removed", Toast.LENGTH_LONG).show();
                            });

                        } catch (RepositoryRequestException e) {
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Could not fetch data from repository.", Toast.LENGTH_LONG).show());
                        }
                    }
                }.start();
            }
        });

    }

}