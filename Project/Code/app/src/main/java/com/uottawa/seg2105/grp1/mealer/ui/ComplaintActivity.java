package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.Complaint;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.User;
import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

public class ComplaintActivity extends AppCompatActivity {

    private TextView cookName;
    private TextView clientName;
    private TextView complaintTitle;
    private TextView complaintDescription;

    private String complaintID;
    private Complaint complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            complaintID = extras.getString("complaintId");
        }

        cookName = (TextView) findViewById(R.id.txtCook);
        clientName = (TextView) findViewById(R.id.txtClient);
        complaintTitle = (TextView) findViewById(R.id.txtTitle);
        complaintDescription = (TextView) findViewById(R.id.txtComplaint);

        new Thread() {
            @Override
            public void run() {
                try {
                    IRepository rep = MealerSystem.getSystem().getRepository();
                    complaint = rep.getById(Complaint.class, complaintID);

                    User cook = complaint.getCook();

                    String cookNameStr = cook.getFirstName() +" "+ cook.getLastName();
                    String clientNameStr = complaint.getClient().getFirstName() + " " + complaint.getClient().getLastName();

                    runOnUiThread(() -> {
                        cookName.setText(cookNameStr);
                        complaintTitle.setText(complaint.getTitle());
                        complaintDescription.setText(complaint.getDescription());
                        clientName.setText(clientNameStr);
                    });

                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Could not get data from repository.", Toast.LENGTH_LONG).show();
                    });
                }
            }

        }.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) return;
        try {
            complaint.archive();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();
    }

    public void btnClose(View view){
        finish();
    }

    public void btnSuspension(View view){
        Intent intent = new Intent(getApplicationContext(), SuspensionActivity.class);
        intent.putExtra("cookId", complaint.getCook().getId());
        startActivityForResult(intent, 0);
    }

    public void btnDismiss(View view) throws InterruptedException {
        complaint.archive();
        finish();
    }
}