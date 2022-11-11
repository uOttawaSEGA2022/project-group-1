package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
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

public class MealListActivity extends AppCompatActivity {

    private String cookID;

    ListView listViewMeals;
    Button createMealBtn;

    List<Meal> meals;

    String TAG = "MealListActivity";

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
            mealInStock.setTag(position);
            removeMealBtn.setTag(position);
            return listViewItem;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);

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

        //TODO onLongClick show dialog with detailed view of meal
        /*listViewMeals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal meal = meals.get(i);
                showUpdateDeleteDialog(meal.getId(), meal.getName());
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
                        MealList productsAdapter = new MealList(MealListActivity.this, meals);
                        listViewMeals.setAdapter(productsAdapter);

                    });

                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Could not fetch data from repository.", Toast.LENGTH_LONG).show());
                }
            }
        }.start();
    }

    public void toggleInStock(View view) {

        int position = (int) view.findViewById(R.id.mealInStockCheck).getTag();
        Log.d(TAG, "meal position selected (stock): "+position);

        //TODO
        //block view: view.setEnabled(false);
        //via DB: change isOffered to false/true for meal
        //unblock view when done: view.setEnabled(true);
        //then maybe send a Toast
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
        final Button removeConfirm = (Button) dialogView.findViewById(R.id.deleteMealConfirmBtn);

        delMealName.setText(meals.get(mealPosition).getName());
        delMealPrice.setText("$"+meals.get(mealPosition).getPrice());

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        removeConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO delete meal from DB
                Log.d(TAG, "Removing meal with ID: "+meals.get(mealPosition).getId());
            }
        });

    }

}