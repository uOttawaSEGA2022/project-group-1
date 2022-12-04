package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.Meal;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.PurchaseRequest;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

public class PurchaseActivity extends AppCompatActivity {
    private String mealId;
    private Meal meal;

    private FragmentManager fm;
    private SpinnerDialog spinner;

    private TextView mealNameTx;
    private TextView mealCookTx;
    private EditText mealQuantityEtx;

    private String TAG = "PurchaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        fm = getSupportFragmentManager();
        spinner = new SpinnerDialog();
        spinner.show(fm, "some_tag");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mealId = null;
            } else {
                mealId = extras.getString("mealId");
            }
        } else {
            mealId = (String) savedInstanceState.getSerializable("mealId");
        }

        mealNameTx = (TextView) findViewById(R.id.purchaseTitle);
        mealCookTx = (TextView) findViewById(R.id.purchaseCook);
        mealQuantityEtx = (EditText) findViewById(R.id.purchaseQuantity);
    }

    protected void onStart() {
        super.onStart();

        new Thread() {
            @Override
            public void run() {
                try {
                    meal = MealerSystem.getSystem().getRepository().getById(Meal.class, mealId);

                    runOnUiThread(() -> {
                        mealNameTx.setText(meal.getName());
                        mealCookTx.setText(String.format("%s %s", meal.getCook().getFirstName(), meal.getCook().getLastName()));
                        spinner.dismiss();
                    });

                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Could not fetch data from repository.", Toast.LENGTH_SHORT).show();
                        spinner.dismiss();
                        finish();
                    });
                }
            }
        }.start();
    }

    public void onPurchaseClick(View view) {
        if (!isValidQuery())
            return;

        final int quantity;
        try {
            quantity = Integer.parseUnsignedInt(mealQuantityEtx.getText().toString());
        } catch (NumberFormatException e) {
            runOnUiThread(() -> {
                mealQuantityEtx.setError("Please very your quantity");
                spinner.dismiss();
            });
            return;
        }

        final float price = quantity * (meal.getPrice() / 100f);

        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set title
        builder.setTitle(R.string.purchaseact_dialogtitle);

        // set message
        builder.setMessage(String.format(getResources().getString(R.string.purchaseact_dialog),
                meal.getName(),
                quantity,
                price
                ));

        // set cancel button
        builder.setNeutralButton(R.string.btnCancel, null);

        // set positive
        builder.setPositiveButton(R.string.btnBuy, (DialogInterface dialogInterface, int i) -> {
            spinner.show(fm, "some_tag");

            new Thread() {
                @Override
                public void run() {
                    try {
                        for (int rep = 0; rep < quantity; rep++) {
                            PurchaseRequest request = PurchaseRequest.create(
                                meal.getCook().getId(),
                                MealerSystem.getSystem().getCurrentUser().getId(),
                                meal.getId());
                        }

                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "Your request has been sent to the cook and is pending.", Toast.LENGTH_LONG).show();
                            spinner.dismiss();
                            finish();
                        });
                    } catch (RepositoryRequestException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "Could not fetch data from repository.", Toast.LENGTH_SHORT).show();
                            spinner.dismiss();
                        });
                    }
                }
            }.start();
        });

        // show dialog
        builder.show();
    }

    private boolean isValidQuery() {
        try {
            int quantity = Integer.parseUnsignedInt(mealQuantityEtx.getText().toString());

            if (quantity <= 0) {
                mealQuantityEtx.setError("Please enter a non-zero quantity.");
                return false;
            } else if (quantity > 10) {
                mealQuantityEtx.setError("Cannot buy more than 10 items.");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            mealQuantityEtx.setError("Please enter a valid integer quantity.");
            return false;
        }
    }

    public void onCancelClick(View view) { finish(); }
}