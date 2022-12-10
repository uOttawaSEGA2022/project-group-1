package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.ClientRole;
import com.uottawa.seg2105.grp1.mealer.lib.Utility;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.User;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreditCardActivity extends AppCompatActivity {
    private FragmentManager fm;
    private SpinnerDialog spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);

        fm = getSupportFragmentManager();
        spinner = new SpinnerDialog();
    }

    public void onSave(View view) {
        EditText cardNum = findViewById(R.id.modifyCreditCard);
        String num = cardNum.getText().toString().trim();

        if (!Utility.isValidField(num,Utility.CREDITCARD)) {
            cardNum.setError("Invalid Card Number");
            return;
        }
        spinner.show(fm, "some_tag");

        new Thread() {
            @Override
            public void run() {
                try {
                    // Fetch the user
                    User client = MealerSystem.getSystem().getCurrentUser();

                    ClientRole role = (ClientRole) client.getRole();

                    // Update their description
                    role.setCardNumber(num);

                    runOnUiThread(() -> {
                        spinner.dismiss();
                        Toast.makeText(getApplicationContext(), "Credit Card has been updated.", Toast.LENGTH_SHORT).show();
                    });
                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> {
                        spinner.dismiss();
                        Toast.makeText(getApplicationContext(), "Could not update Credit Card.", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }.start();
    }

    public void onCancel(View view) {
        finish();
    }
}