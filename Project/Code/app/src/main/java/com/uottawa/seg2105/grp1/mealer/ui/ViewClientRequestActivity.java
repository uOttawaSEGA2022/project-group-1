package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.Meal;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.PurchaseRequest;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewClientRequestActivity extends AppCompatActivity {

    private String clientID;

    private ListView listViewRequests;
    private Button backBtn;

    private SpinnerDialog spinner;

    private ClientRequestList purchaseAdapter;
    private List<PurchaseRequest> requests;
    private Map<String, Meal> meals;

    private String TAG = "ViewClientRequestActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_client_request);

        //Cannot interact with window until onStart() finishes successfully
        FragmentManager fm = getSupportFragmentManager();
        spinner = new SpinnerDialog();
        spinner.show(fm, "some_tag");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                clientID = null;
            } else {
                clientID = extras.getString("clientId");
            }
        } else {
            clientID = (String) savedInstanceState.getSerializable("clientId");
        }

        listViewRequests = (ListView) findViewById(R.id.clientReqList);
        backBtn = (Button) findViewById(R.id.clientReqBackBtn);

        requests = new ArrayList<>();
    }


    @Override
    protected void onStart() {
        super.onStart();

        new Thread() {
            @Override
            public void run() {

                try {
                    requests = MealerSystem.getSystem().getRepository().query(
                            PurchaseRequest.class,
                            (p) -> p.getClientEmail().equals(clientID)
                    );

                    ArrayList<String> mealIds = new ArrayList<>();
                    for (PurchaseRequest req : requests)
                        if (!mealIds.contains(req.getMealId())) mealIds.add(req.getMealId());

                    List<Meal> tmpMeals = MealerSystem.getSystem().getRepository().query(
                            Meal.class,
                            (m) -> true
                    );

                    meals = new HashMap<>();
                    for (String Id : mealIds) {
                        for (Meal meal : tmpMeals) {
                            if (meal.getId().equals(Id))
                                meals.put(Id, meal);
                        }
                    }

                    runOnUiThread(() -> {
                        purchaseAdapter = new ClientRequestList(ViewClientRequestActivity.this, requests);
                        listViewRequests.setAdapter(purchaseAdapter);

                        //Window interactable once meals are loaded up
                        spinner.dismiss();

                    });

                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Could not fetch data from repository.", Toast.LENGTH_LONG).show());
                }
            }
        }.start();

    }

    //Adapter class for listView
    public class ClientRequestList extends ArrayAdapter<PurchaseRequest> {
        private Activity context;

        List<PurchaseRequest> requests;
        TextView mealName, cookName, mealState;
        ImageButton rateBtn, complainBtn;


        public ClientRequestList(Activity context, List<PurchaseRequest> requests) {
            super(context, R.layout.layout_client_request_item, requests);
            this.context = context;
            this.requests = requests;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_client_request_item, null, true);

            mealName = (TextView) listViewItem.findViewById(R.id.clientReqItemName);
            cookName = (TextView) listViewItem.findViewById(R.id.clientReqItemCook);
            mealState = (TextView) listViewItem.findViewById(R.id.clientReqState);
            rateBtn = (ImageButton) listViewItem.findViewById(R.id.clientReqRateBtn);
            complainBtn = (ImageButton) listViewItem.findViewById(R.id.clientReqComplainBtn);

            PurchaseRequest request = requests.get(position);
            Meal meal = meals.get(request.getMealId());

            mealName.setText(meal.getName());
            cookName.setText(meal.getCook().getFirstName() + " " + meal.getCook().getLastName());

            PurchaseRequest.Status mealStatus =  request.getStatus();
            if (mealStatus == PurchaseRequest.Status.COMPLETE)
                mealState.setText("Approved");
            else if (mealStatus == PurchaseRequest.Status.PENDING)
                mealState.setText("Pending");
            else
                mealState.setText("Rejected");

            //Just store position where invisible
            rateBtn.setTag(position);
            complainBtn.setTag(position);

            return listViewItem;
        }
    }

    //Buttons
    public void onBack(View view) {
        finish();
    }

    //Starts ClientComplaintActivity
    public void complainBtn(View view){

        int position = (int) view.findViewById(R.id.clientReqComplainBtn).getTag();
        PurchaseRequest tmpReq = requests.get(position);

        if (!tmpReq.getIsComplained() && tmpReq.getStatus() == PurchaseRequest.Status.COMPLETE) {
            Intent intent = new Intent(getApplicationContext(), ClientComplaint.class);
            intent.putExtra("purchaseId", requests.get(position).getId());
            startActivity(intent);
        } else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            dialogBuilder
                    .setMessage(tmpReq.getStatus() == PurchaseRequest.Status.COMPLETE ? "Already complained about this meal!" : "Cannot rate an unapproved meal")
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }

    //Calls either 3 methods below depending whether purchase is complet, rated or not
    public void rateBtn(View view) {

        int position = (int) view.findViewById(R.id.clientReqRateBtn).getTag();
        PurchaseRequest tmpReq = requests.get(position);
        Meal meal = meals.get(tmpReq.getMealId());

        if (tmpReq.getStatus() != PurchaseRequest.Status.COMPLETE)
            showUnapprovedDialog();
        else if (tmpReq.getIsRated())
            showAlreadyRatedDialog();
        else
            showRateDialog(tmpReq, meal, position);

    }
    private void showUnapprovedDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder
                .setMessage("Only approved meals can be rated!")
                .setNegativeButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void showAlreadyRatedDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder
                .setMessage("Already rated this meal!")
                .setNegativeButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void showRateDialog(PurchaseRequest request, Meal meal, int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.rate_cook_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView mealName = (TextView) dialogView.findViewById(R.id.rateMealName);
        final TextView cookName  = (TextView) dialogView.findViewById(R.id.rateMealCook);
        final RatingBar ratingBar  = (RatingBar) dialogView.findViewById(R.id.rateMealBar);
        final Button rateBtn = (Button) dialogView.findViewById(R.id.itemRateBtn);

        mealName.setText(meal.getName());
        cookName.setText(meal.getCook().getFirstName() + " " + meal.getCook().getLastName());

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "Rating cook with ID: "+meal.getCook().getId());

                FragmentManager fm = getSupportFragmentManager();
                spinner = new SpinnerDialog();
                spinner.show(fm, "some_tag");

                new Thread() {
                    @Override
                    public void run() {

                        try {

                            request.rate((int) ratingBar.getRating());

                            PurchaseRequest updatedRequest =
                                    MealerSystem.getSystem().getRepository().getById(
                                    PurchaseRequest.class, request.getId()
                            );

                            runOnUiThread(() -> {
                                requests.set(position, updatedRequest);
                                purchaseAdapter.notifyDataSetChanged();

                                spinner.dismiss();
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Meal successfully rated!", Toast.LENGTH_LONG).show();
                            });

                        } catch (RepositoryRequestException e) {
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Could not fetch data from repository.", Toast.LENGTH_LONG).show());
                        }
                    }
                }.start();
            }
        });

    }

}