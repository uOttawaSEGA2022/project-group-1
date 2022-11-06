package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.lib.Utility;
import com.uottawa.seg2105.grp1.mealer.model.Complaint;
import com.uottawa.seg2105.grp1.mealer.model.CookRole;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.User;
import com.uottawa.seg2105.grp1.mealer.model.UserRole;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uottawa.seg2105.grp1.mealer.R;

public class ComplaintActivity extends AppCompatActivity {

    public static TextView banDateText;
    public static long banDateAsLong;

    private TextView banReason;
    private ToggleButton banPermaToggle;
    private CheckBox banConfirmBox;
    private TextView cookName;
    private TextView clientName;
    private TextView textComplaint;

    private String cookID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);


        cookName = (TextView) findViewById(R.id.txtCook);
        clientName = (TextView) findViewById(R.id.txtClient);
        textComplaint = (TextView) findViewById(R.id.txtComplaint);
        banReason = (TextView) findViewById(R.id.txtTitle);

        new Thread() {
            @Override
            public void run() {
                try {
                    cookID = "cooktest@email.com";//Connect it so it checks for who the actual ID is

                    User cook = MealerSystem.getSystem().getRepository().getById(User.class,cookID);

                    Map<String, Object> properties = cook.serialise();
                    Map<String, Object> roleData = (HashMap<String, Object>) properties.get("role");

                    System.out.println(roleData.get("banReason"));

                    String name = cook.getFirstName() +" "+ cook.getLastName();
                    System.out.println(name);



                    //TODO: find out what the problem is
                    //ERROR: Only the original thread that created a view hierarchy can touch its views. <---
                    cookName.setText(name);
                    banReason.setText(String.valueOf(roleData.get("banReason")));
                    textComplaint.setText(String.valueOf(roleData.get("description"))); //this one crashes if the description is too long, will need to fix
                    clientName.setText("Client not implemented yet?");

                } catch (RepositoryRequestException e) {
                    Toast.makeText(getApplicationContext(), "Could not get data from repository.", Toast.LENGTH_LONG).show();
                }
            }

        }.start();

    }

    public void btnClose(View view){
        finish();
    }

    public void btnSuspesnsion(View view){
        //TODO: This still needs to be done

        Intent intent = new Intent(getApplicationContext(), SuspensionActivity.class);
        startActivity(intent);
    }

    public void btnDismiss(View view){

        //TODO: This still needs to be done

    }


}