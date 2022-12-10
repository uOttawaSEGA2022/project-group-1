package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.Meal;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.PurchaseRequest;
import com.uottawa.seg2105.grp1.mealer.model.User;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

public class ClientComplaint extends AppCompatActivity {
    private PurchaseRequest request;
    private EditText complaintTxt;
    private EditText complaintTitleTxt;
    private FragmentManager fm;
    private SpinnerDialog spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String purchaseId = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_complaint);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                purchaseId = null;
            } else {
                purchaseId = extras.getString("purchaseId");
            }
        } else {
            purchaseId = (String) savedInstanceState.getSerializable("purchaseId");
        }

        if (purchaseId == null)
            throw new RuntimeException("[ClientComplaint] purchaseId must be specified");

        // Create SpinnerDialog
        fm = getSupportFragmentManager();
        spinner = new SpinnerDialog();
        spinner.show(fm, "some_tag");

        // Set description
        complaintTxt = findViewById(R.id.complaintTxt);
        complaintTitleTxt = findViewById(R.id.complaintTitleTxt);
        TextView cookTxt = findViewById(R.id.complaintCookName);
        TextView mealTxt = findViewById(R.id.complaintMealName);
        final String pId = purchaseId;

        new Thread() {
            @Override
            public void run() {
                try {
                    request = MealerSystem.getSystem().getRepository().getById(PurchaseRequest.class, pId);
                    User cook = MealerSystem.getSystem().getRepository().getById(User.class, request.getCookEmail());
                    Meal meal = MealerSystem.getSystem().getRepository().getById(Meal.class, request.getMealId());
                    String cookName = cook.getFirstName() + " " + cook.getLastName();
                    String mealName = meal.getName();
                    runOnUiThread(() -> {
                        spinner.dismiss();
                        cookTxt.setText(cookName);
                        mealTxt.setText(mealName);
                    });
                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> {
                        spinner.dismiss();
                        Toast.makeText(getApplicationContext(), "Could not update complaint.", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }.start();
    }



    public void onSave(View view) {
        String complaintDesc = complaintTxt.getText().toString().trim();
        spinner.show(fm, "some_tag");
        String complaintTitle = complaintTitleTxt.getText().toString().trim();
        String complaint = complaintTxt.getText().toString().trim();

        new Thread() {
            @Override
            public void run() {
                try {
                    request.complain(complaintTitle, complaint, request.getClientEmail(), request.getCookEmail());
                    runOnUiThread(() -> {
                        spinner.dismiss();
                        Toast.makeText(getApplicationContext(), "Complaint has been sent.", Toast.LENGTH_SHORT).show();
                    });
                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> {
                        spinner.dismiss();
                        Toast.makeText(getApplicationContext(), "Could not send complaint.", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }.start();
    }

    public void onCancel(View view) {
        finish();
    }
}