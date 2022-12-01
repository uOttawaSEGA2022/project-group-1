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
import com.uottawa.seg2105.grp1.mealer.model.ClientRole;
import com.uottawa.seg2105.grp1.mealer.model.CookRole;
import com.uottawa.seg2105.grp1.mealer.model.Meal;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.PurchaseRequest;
import com.uottawa.seg2105.grp1.mealer.model.User;
import com.uottawa.seg2105.grp1.mealer.model.UserRole;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewCookRequestActivity extends AppCompatActivity {

    private String cookID;

    private ListView listViewRequests;
    private Button backBtn;

    private SpinnerDialog spinner;

    private CookRequestList purchaseAdapter;
    private List<PurchaseRequest> requests;
    private Map<String, Meal> meals;
    private Map<String, User> clients;

    private String TAG = "ViewCookRequestActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cook_request);

        //Cannot interact with window until onStart() finishes successfully
        FragmentManager fm = getSupportFragmentManager();
        spinner = new SpinnerDialog();
        spinner.show(fm, "some_tag");

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

        listViewRequests = (ListView) findViewById(R.id.cookReqList);
        backBtn = (Button) findViewById(R.id.cookReqBackBtn);

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
                            (p) -> p.getCookEmail().equals(cookID)
                    );

                    //List of unique mealIds & clientIds extracted from requests
                    ArrayList<String> mealIds = new ArrayList<>();
                    ArrayList<String> clientIds = new ArrayList<>();
                    for (PurchaseRequest req : requests) {
                        if (!mealIds.contains(req.getMealId())) mealIds.add(req.getMealId());
                        if (!clientIds.contains(req.getClientEmail())) clientIds.add(req.getClientEmail());
                    }

                    //Getting relevant meals
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

                    //Getting relevant clients
                    List<User> tmpUsers = MealerSystem.getSystem().getRepository().query(
                            User.class,
                            (m) -> true
                    );
                    clients = new HashMap<>();
                    for (String Id : clientIds) {
                        for (User user : tmpUsers) {
                            if (user.getId().equals(Id))
                                clients.put(Id, user);
                        }
                    }

                    runOnUiThread(() -> {
                        purchaseAdapter = new CookRequestList(ViewCookRequestActivity.this, requests);
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
    public class CookRequestList extends ArrayAdapter<PurchaseRequest> {
        private Activity context;

        List<PurchaseRequest> requests;
        TextView mealName, clientName, mealState;
        ImageButton stateDialogBtn;


        public CookRequestList(Activity context, List<PurchaseRequest> requests) {
            super(context, R.layout.layout_cook_request_item, requests);
            this.context = context;
            this.requests = requests;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_cook_request_item, null, true);

            mealName = (TextView) listViewItem.findViewById(R.id.cookReqMealName);
            clientName = (TextView) listViewItem.findViewById(R.id.cookReqClientName);
            mealState = (TextView) listViewItem.findViewById(R.id.cookReqState);
            stateDialogBtn = (ImageButton) listViewItem.findViewById(R.id.cookReqStateBtn);

            PurchaseRequest request = requests.get(position);
            Meal meal = meals.get(request.getMealId());
            User client = clients.get(requests.get(position).getClientEmail());

            mealName.setText(meal.getName());
            clientName.setText(client.getFirstName() + " " + client.getLastName());

            PurchaseRequest.Status mealStatus =  request.getStatus();
            if (mealStatus == PurchaseRequest.Status.COMPLETE)
                mealState.setText("Approved");
            else if (mealStatus == PurchaseRequest.Status.PENDING)
                mealState.setText("Pending");
            else
                mealState.setText("Rejected");

            //Just store position where invisible
            stateDialogBtn.setTag(position);

            return listViewItem;
        }
    }

    //Buttons
    public void onBack(View view) {
        finish();
    }

    //If request is pending, call showRejectApproveDialog, else showNotPendingDialog
    public void stateDialogBtn(View view) {

        int position = (int) view.findViewById(R.id.cookReqStateBtn).getTag();
        PurchaseRequest tmpReq = requests.get(position);
        Meal meal = meals.get(tmpReq.getMealId());
        User client = clients.get(tmpReq.getClientEmail());

        if (tmpReq.getStatus() != PurchaseRequest.Status.PENDING)
            showNotPendingDialog(tmpReq.getStatus());
        else
            showRejectApproveDialog(tmpReq, meal, client, position);

    }
    private void showNotPendingDialog(PurchaseRequest.Status status) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        String message = "Request already ";
        if (status == PurchaseRequest.Status.COMPLETE)
            message += "approved!";
        else
            message += "rejected!";

        dialogBuilder
                .setMessage(message)
                .setNegativeButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void showRejectApproveDialog(PurchaseRequest request, Meal meal, User client, int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.reject_approve_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView mealName = (TextView) dialogView.findViewById(R.id.approveMealName);
        final TextView clientName  = (TextView) dialogView.findViewById(R.id.approveClientName);
        final Button rejectBtn = (Button) dialogView.findViewById(R.id.rejectMealBtn);
        final Button approveBtn = (Button) dialogView.findViewById(R.id.approveMealBtn);

        mealName.setText(meal.getName());
        clientName.setText(client.getFirstName() + " " + client.getLastName());

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "Rejecting request with ID: "+request.getId());

                FragmentManager fm = getSupportFragmentManager();
                spinner = new SpinnerDialog();
                spinner.show(fm, "some_tag");

                new Thread() {
                    @Override
                    public void run() {

                        try {
                            //Change purchase status to rejected/complete
                            //Increase cook meals sold by 1
                            Map<String, Object> updatedReqProperties = request.serialise();
                            updatedReqProperties.replace("status", PurchaseRequest.Status.REJECTED);

                            MealerSystem.getSystem().getRepository().update(
                                    PurchaseRequest.class, request.getId(), updatedReqProperties
                            );

                            PurchaseRequest updatedRequest =
                                    MealerSystem.getSystem().getRepository().getById(
                                            PurchaseRequest.class, request.getId()
                                    );

                            runOnUiThread(() -> {
                                requests.set(position, updatedRequest);
                                purchaseAdapter.notifyDataSetChanged();

                                spinner.dismiss();
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Order rejected", Toast.LENGTH_LONG).show();
                            });

                        } catch (RepositoryRequestException e) {
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Could not fetch data from repository.", Toast.LENGTH_LONG).show());
                        }
                    }
                }.start();
            }
        });

        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "Approving request with ID: "+request.getId());

                FragmentManager fm = getSupportFragmentManager();
                spinner = new SpinnerDialog();
                spinner.show(fm, "some_tag");

                new Thread() {
                    @Override
                    public void run() {

                        try {
                            //Change purchase status to rejected/complete
                            Map<String, Object> updatedReqProps = request.serialise();
                            updatedReqProps.replace("status", PurchaseRequest.Status.COMPLETE);

                            MealerSystem.getSystem().getRepository().update(
                                    PurchaseRequest.class, request.getId(), updatedReqProps
                            );

                            PurchaseRequest updatedRequest =
                                    MealerSystem.getSystem().getRepository().getById(
                                            PurchaseRequest.class, request.getId()
                                    );

                            //Increase cook meals sold by 1
                            Meal oldMeal = meals.get(request.getMealId());
                            User cook = oldMeal.getCook();
                            Map<String, Object> updatedCookProps = cook.serialise();
                            Map<String, Object> roleData = (HashMap<String, Object>) updatedCookProps.get("role");

                            int newMealsSold = ((Long) roleData.get("mealsSold")).intValue() + 1;
                            roleData.replace("mealsSold", newMealsSold);
                            updatedCookProps.replace("role", roleData);

                            MealerSystem.getSystem().getRepository().update(
                                    User.class, cook.getId(), updatedCookProps
                            );

                            //Update adapter for meals cause cook changed
                            Meal updatedMeal =
                                    MealerSystem.getSystem().getRepository().getById(
                                            Meal.class, oldMeal.getId()
                                    );

                            runOnUiThread(() -> {
                                meals.replace(oldMeal.getId(), updatedMeal);
                                requests.set(position, updatedRequest);
                                purchaseAdapter.notifyDataSetChanged();

                                spinner.dismiss();
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Order approved!", Toast.LENGTH_LONG).show();
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