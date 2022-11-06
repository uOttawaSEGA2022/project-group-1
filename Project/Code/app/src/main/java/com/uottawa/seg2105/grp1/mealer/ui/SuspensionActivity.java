package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class SuspensionActivity extends AppCompatActivity {

    //Set public & static for datePick fragment to edit
    @SuppressLint("StaticFieldLeak")
    public static TextView banDateText;
    public static long banDateAsLong;

    private TextView banCookName;
    private TextView banCookEmail;
    private EditText banReason;
    private ToggleButton banPermaToggle;
    private CheckBox banConfirmBox;

    private String cookID;
    private User cook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspension);

        banDateText = (TextView) findViewById(R.id.banDateText);

        banCookName = (TextView) findViewById(R.id.banCookName);
        banCookEmail = (TextView) findViewById(R.id.banCookEmail);
        banReason = (EditText) findViewById(R.id.banReason);
        banPermaToggle = (ToggleButton) findViewById(R.id.banPermaToggle);
        banConfirmBox = (CheckBox) findViewById(R.id.banConfirmBox);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                cookID = null;
            } else {
                cookID = extras.getString("cookId");
                try {
                    cook = MealerSystem.getSystem().getRepository().getById(
                            User.class,
                            cookID
                    );
                    banCookName.setText(cook.getFirstName() +" "+ cook.getLastName());
                    banCookEmail.setText(cook.getEmail());
                } catch (RepositoryRequestException e) {
                    e.printStackTrace();
                }
            }
        } else {
            cookID = (String) savedInstanceState.getSerializable("cookId");
            try {
                cook = MealerSystem.getSystem().getRepository().getById(
                        User.class,
                        cookID
                );
                banCookName.setText(cook.getFirstName() +" "+ cook.getLastName());
                banCookEmail.setText(cook.getEmail());
            } catch (RepositoryRequestException e) {
                e.printStackTrace();
            }
        }
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void banBtn(View view) {
        if (banFormIsValid()) {

            view.setEnabled(false);

            new Thread() {
                @Override
                public void run() {

                    try {

                        Map<String, Object> properties = cook.serialise();
                        Map<String, Object> roleData = (HashMap<String, Object>) properties.get("role");

                        roleData.replace("banReason", banReason.getText().toString());
                        roleData.replace("banExpiration", getDateAsLong());

                        properties.replace("role", roleData);//Finally, put roleData to properties

                        MealerSystem.getSystem().getRepository().update(User.class, cookID, properties);

                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();//End this activity & return to ComplaintActivity (must finish() that as well)
                    } catch (RepositoryRequestException e) {

                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, resultIntent);
                        finish();
                    }
                }
            }.start();

        } else {
            Toast.makeText(SuspensionActivity.this, "Please complete required fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean banFormIsValid() {

        boolean result = true;

        //Cases for banReason: empty & < 10 chars
        if(Utility.isEmpty(banReason)) {
            banReason.setError("Ban reason required");
            result = false;
        } else if (banReason.getText().toString().length() < 10) {
            banReason.setError("Include at least 10 characters");
            result = false;
        }

        //Case where the ban is not permanent and no date included
        if (banDateText.getText().toString().charAt(0) == '('
         && banPermaToggle.getText().toString().equals("NO")) {
            banDateText.setError("Not a permaban; choose a date");
            result = false;
        }

        //Check that confirmation box is checked
        if (!banConfirmBox.isChecked()) {
            banConfirmBox.setError("Confirmation required");
            result = false;
        }

        return result;
    }

    //If ban toggle is 'YES', banDate's text changes accordingly
    //to guide the user
    public void banToggleBtn(View view) {
        if (banPermaToggle.getText().toString().equals("NO")) {
            banDateText.setText("(Pick a date)");
        } else {
            banDateText.setText("(Not applicable)");
        }
    }

    public void banCheckBoxBtn(View view) {
        if (!banConfirmBox.isChecked()) {
            banConfirmBox.setText("Confirm Ban");
        } else {
            banConfirmBox.setText("Ban Confirmed");
        }
    }

    //Returns 0 if permabanned, or a long if
    //not a permaban (date specified)
    private long getDateAsLong() {
        if (banPermaToggle.getText().toString().equals("NO")) {
            return banDateAsLong;
        } else {
            return 0;
        }
    }

}