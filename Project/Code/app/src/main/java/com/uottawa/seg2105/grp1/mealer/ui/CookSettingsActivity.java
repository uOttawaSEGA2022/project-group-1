package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.CookRole;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.User;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

public class CookSettingsActivity extends AppCompatActivity {
    private String cookID;
    private EditText description;
    private FragmentManager fm;
    private SpinnerDialog spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_settings);

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

        if (cookID == null)
            throw new RuntimeException("[CookSettingsActivity] cookID must be specified");

        // Create SpinnerDialog
        fm = getSupportFragmentManager();
        spinner = new SpinnerDialog();

        // Set description
        spinner.show(fm, "some_tag");
        description = findViewById(R.id.modifyCookDescription);

        new Thread() {
            @Override
            public void run() {
                try {
                    // Fetch the user
                    User cook = MealerSystem.getSystem().getRepository().getById(User.class, cookID);
                    if (cook == null)
                        throw new RuntimeException("[CookSettingsActivity] cook '" + cookID + "' cannot be fetched from the database");

                    if (!(cook.getRole() instanceof CookRole))
                        throw new RuntimeException("[CookSettingsActivity] cook '" + cookID + "' does not have a cook role");
                    CookRole role = (CookRole) cook.getRole();
                    runOnUiThread(() -> {
                        spinner.dismiss();
                        description.setText(role.getDescription());
                    });
                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> {
                        spinner.dismiss();
                        Toast.makeText(getApplicationContext(), "Could not update description.", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }.start();
    }

    public void onSave(View view) {
        spinner.show(fm, "some_tag");

        new Thread() {
            @Override
            public void run() {
                try {
                    // Fetch the user
                    User cook = MealerSystem.getSystem().getRepository().getById(User.class, cookID);
                    if (cook == null)
                        throw new RuntimeException("[CookSettingsActivity] cook '" + cookID + "' cannot be fetched from the database");

                    if (!(cook.getRole() instanceof CookRole))
                        throw new RuntimeException("[CookSettingsActivity] cook '" + cookID + "' does not have a cook role");
                    CookRole role = (CookRole) cook.getRole();

                    // Update their description
                    role.setDescription(description.getText().toString().trim());

                    runOnUiThread(() -> {
                        spinner.dismiss();
                        Toast.makeText(getApplicationContext(), "Description has been updated.", Toast.LENGTH_SHORT).show();
                    });
                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> {
                        spinner.dismiss();
                        Toast.makeText(getApplicationContext(), "Could not update description.", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }.start();
    }

    public void onCancel(View view) {
        finish();
    }
}