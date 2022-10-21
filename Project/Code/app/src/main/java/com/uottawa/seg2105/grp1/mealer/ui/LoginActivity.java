package com.uottawa.seg2105.grp1.mealer.ui;

// TODO: Create AdminHome, CookHome

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.ClientRole;
import com.uottawa.seg2105.grp1.mealer.model.CookRole;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread() {
            @Override
            public void run() {
                MealerSystem system = MealerSystem.getSystem();
                system.restoreSession();

                User currentUser = system.getCurrentUser();

                if (currentUser == null) return;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentUser.isAdmin()) {
                            // TODO: Add intent here when Administrator home is done
                            Toast.makeText(LoginActivity.this, "TODO: Go to admin home", Toast.LENGTH_LONG).show();
                        } else {
                            if (currentUser.getRole() instanceof ClientRole) {
                                Intent intent = new Intent(getApplicationContext(), ClientHome.class);
                                startActivity(intent);
                            } else if (currentUser.getRole() instanceof CookRole) {
                                // TODO: Add intent here when Cook home is done
                                Toast.makeText(LoginActivity.this, "TODO: Go to cook home", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        }.start();
    }

    private static final String TAG = "LoginActivity";

    //Just a test method; you may remove it
    //Adds a user to the collection "users" to the Cloud Firestore DB
    public void testBtn(View view) {

        //Get instance of our DB
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirestoreTest test = new FirestoreTest(db);

        //Adds a user to the DB
        test.addUser(view);
    }
    //btn that opens a loginpage class
    public void btnLoginPage(View view){
        System.out.println("loginpage");
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
    }
    //btn that opens a cookregister class
    public void btnRegisterCook(View view){
        System.out.println("cook");
        Intent intent = new Intent(getApplicationContext(), CookRegister.class);
        startActivity(intent);
    }
    //btn that opens a cookClient class
    public void btnRegisterClient(View view){
        System.out.println("client");
        Intent intent = new Intent(getApplicationContext(), ClientRegister.class);
        startActivity(intent);
    }


}