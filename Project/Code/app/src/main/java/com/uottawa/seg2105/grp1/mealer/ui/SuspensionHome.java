package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.uottawa.seg2105.grp1.mealer.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SuspensionHome extends AppCompatActivity {

    private TextView banDescription;
    private Long banExpiration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspension_home);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                banExpiration = null;
            } else {
                banExpiration = extras.getLong("banExpiration");
                if (banExpiration > 0) {
                    Date date = new Date(banExpiration);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    banDescription.setText("You are temporarily banned until: " + sdf.format(date));
                } else {
                    banDescription.setText("You are permanently banned.");
                }
            }
        } else {
            banExpiration = (Long) savedInstanceState.getSerializable("banExpiration");
            if (banExpiration > 0) {
                Date date = new Date(banExpiration);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                banDescription.setText("You are temporarily banned until: " + sdf.format(date));
            } else {
                banDescription.setText("You are permanently banned.");
            }
        }
    }
}