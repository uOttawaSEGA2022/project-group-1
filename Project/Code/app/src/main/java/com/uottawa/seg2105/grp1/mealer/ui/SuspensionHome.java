package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SuspensionHome extends AppCompatActivity {

    private TextView tvBanDescription;
    private TextView tvBanReason;
    private Long banExpiration;
    private String banReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspension_home);
        banDescription = (TextView) findViewById(R.id.banDescription);

        tvBanDescription = (TextView) findViewById(R.id.banDescription);
        tvBanReason = (TextView) findViewById(R.id.tvBanReason);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                banExpiration = null;
                banReason = "";
            } else {
                banExpiration = extras.getLong("banExpiration");
                banReason = extras.getString("banReason");
                if (banExpiration > 0) {
                    Date date = new Date(banExpiration);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    tvBanDescription.setText("You are temporarily banned until: " + sdf.format(date));
                } else {
                    tvBanDescription.setText("You are permanently banned.");
                }

                if (banReason == null || banReason.trim().equals(""))
                    tvBanReason.setText("The administrator has not specified a reason.");
                else
                    tvBanReason.setText(banReason);
            }
        } else {
            banExpiration = (Long) savedInstanceState.getSerializable("banExpiration");
            if (banExpiration > 0) {
                Date date = new Date(banExpiration);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                tvBanDescription.setText("You are temporarily banned until: " + sdf.format(date));
            } else {
                tvBanDescription.setText("You are permanently banned.");
            }
        }
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