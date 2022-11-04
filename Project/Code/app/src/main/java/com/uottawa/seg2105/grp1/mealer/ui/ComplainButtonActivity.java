package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.Complaint;

public class ComplainButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_button);
    }

    public void btnComplain(View view) {
        Complaint c = new Complaint();
        c.create("This is my complaint", "The food was bad", "client@email.com", "cook@email.com");
    }
}