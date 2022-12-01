package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.*;
import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.List;
import java.util.Random;

public class DebugMode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_mode);
    }


    public void btnOpenCook(View view){
        System.out.println("debug");
        Intent intent = new Intent(getApplicationContext(), CookAddMeal.class);
        startActivity(intent);
    }

    // TODO: Remove once ComplaintActivity starts expected a complaintId
    @Deprecated
    public void btnOpenComplaint(View view){
        System.out.println("debug");
        Intent intent = new Intent(getApplicationContext(), ComplaintActivity.class);
        startActivity(intent);
    }

    public void btnRandomComplaint(View view){
        final String[] complaintTitles = {
                "I don't like this cook",
                "The food was not good",
                "This cook didn't deliver the right food",
                "The food was not safe to eat"
        };

        final String[] complaintDescriptions = {
                "The cook was really mean during our exchanges and wasn't cooperative",
                "The food was no good at all. I was expecting at less some standard of quality but I didn't even get that!",
                "The food was disgusting, I want a refund!",
                "I expected better. This cook should not be selling on this platform."
        };

        IRepository rep = MealerSystem.getSystem().getRepository();
        view.setEnabled(false);

        new Thread() {
            @Override
            public void run() {
                try {
                    List<User> clients = rep.query(User.class, (User u) -> u.getRole() instanceof ClientRole);
                    List<User> cooks = rep.query(User.class, (User u) -> { return (u.getRole() instanceof CookRole) && (((CookRole) u.getRole()).getBanExpiration() == -1); });

                    Random rand = new Random();

                    String title = complaintTitles[rand.nextInt(complaintTitles.length)];
                    String description = complaintDescriptions[rand.nextInt(complaintDescriptions.length)];
                    String clientEmail = clients.get(rand.nextInt(clients.size())).getEmail();
                    String cookEmail = cooks.get(rand.nextInt(cooks.size())).getEmail();

                    Complaint c = Complaint.create(title, description, clientEmail, cookEmail);

                    runOnUiThread(() -> {
                       Toast.makeText(getApplicationContext(), "Created random complaint successfully", Toast.LENGTH_LONG).show();
                        view.setEnabled(true);
                    });

                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Couldn't fetch data", Toast.LENGTH_LONG).show());
                    view.setEnabled(true);
                }
            }
        }.start();
    }

    public void btnUnarchiveAll(View view) {
        view.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                try {
                    Complaint.unarchiveAll();
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "All complaints were unarchived", Toast.LENGTH_LONG).show();
                        view.setEnabled(true);
                    });
                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Couldn't update complaints", Toast.LENGTH_LONG).show();
                        view.setEnabled(true);
                    });
                }
            }
        }.start();
    }

    public void btnUnbanAll(View view) {
        // TODO: Unban all cooks
        IRepository rep = MealerSystem.getSystem().getRepository();
        view.setEnabled(false);

        new Thread() {
            @Override
            public void run() {
                try {
                    List<User> bannedCooks = rep.query(User.class, (User u) -> { return (u.getRole() instanceof CookRole) && (((CookRole) u.getRole()).getBanExpiration() != -1); });
                    for (User cook : bannedCooks) {
                        CookRole role = (CookRole) cook.getRole();
                        role.setBan(-1, ""); // Unban the user
                    }

                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "All suspended cooks were unsuspended", Toast.LENGTH_LONG).show();
                        view.setEnabled(true);
                    });
                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Couldn't update banned cooks", Toast.LENGTH_LONG).show();
                        view.setEnabled(true);
                    });
                }
            }
        }.start();
    }

    public void btnMealListActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), MealListActivity.class);
        intent.putExtra("cookId", "cook@email.com");
        startActivity(intent);
    }

    public void createOnePurchaseRequest(View view) {
        IRepository rep = MealerSystem.getSystem().getRepository();
        new Thread() {
            @Override
            public void run() {
                try {
                    //User cook = User.getByEmail("cook@email.com");
                    //User client = User.getByEmail("client@email.com");
                    //Meal meal = Meal.getById("boWcX7v8wfkE6jjkPozx");
                    PurchaseRequest pr = PurchaseRequest.create("cook@email.com", "client@email.com", "boWcX7v8wfkE6jjkPozx");
                    Thread.sleep(2000);
                    pr.approve(true);
                    Thread.sleep(2000);
                    pr.complain("Badness was", "There was badness and I didn't like it",
                            "client@email.com", "cook@email.com");
                    Thread.sleep(2000);
                    pr.rate(5);
                    Thread.sleep(2000);
                    CookRole cookRole = (CookRole) User.getByEmail("cook@email.com").getRole();
                    ClientRole clientRole = (ClientRole) User.getByEmail("client@email.com").getRole();
                    List<PurchaseRequest> cookList = cookRole.getPurchaseRequests();
                    List<PurchaseRequest> clientList = clientRole.getPurchaseRequests();
                    System.out.println("-----------------Cook purchases:-----------------");
                    for (PurchaseRequest p : cookList) {
                        System.out.println(p.getStatus());
                    }
                    System.out.println("------------------Client purchases:-----------------");
                    for (PurchaseRequest p : clientList) {
                        System.out.println(p.getStatus());
                    }
                    List<Meal> mealList = Meal.getOfferedMeals();
                    for (Meal m : mealList) {
                        System.out.println(m.getDescription());
                    }
                } catch (RepositoryRequestException ignored) {
                } catch (InterruptedException e) {
                    e.printStackTrace();
                };
            }
        }.start();
    }


    public void btnClientReqActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewClientRequestActivity.class);
        intent.putExtra("clientId", "client@email.com");
        startActivity(intent);
    }
    public void btnCookReqActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewCookRequestActivity.class);
        intent.putExtra("cookId", "cook@email.com");
        startActivity(intent);
    }

}