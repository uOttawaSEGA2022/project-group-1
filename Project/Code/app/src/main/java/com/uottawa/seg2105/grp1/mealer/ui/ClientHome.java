package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.lib.Utility;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.User;

public class ClientHome extends AppCompatActivity {
    private TextView txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        User user = MealerSystem.getSystem().getCurrentUser();
        String fullname = user.getFirstName() + " " + user.getLastName();

        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        txtWelcome.setText("Welcome, " + fullname);
    }


    //TODO: change deubgmode to the name of the activity
    public void btnOrderHistory(View view){
        Intent intent = new Intent(getApplicationContext(), ViewClientRequestActivity.class);
        intent.putExtra("clientId", MealerSystem.getSystem().getCurrentUser().getId());
        startActivity(intent);
    }

    public void btnSearch(View view){
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
    }

    public void btnPayment(View view){
        Intent intent = new Intent(getApplicationContext(), CreditCardActivity.class);
        intent.putExtra("clientId", MealerSystem.getSystem().getCurrentUser().getId());
        startActivity(intent);
    }


    public void onLogOff(View view) {
        new Thread() {
            @Override
            public void run() {
                MealerSystem.getSystem().logoff();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        }.start();
    }
}