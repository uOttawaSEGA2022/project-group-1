package com.uottawa.seg2105.grp1.mealer.ui;


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
                            Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                            startActivity(intent);
                        } else {
                            if (currentUser.getRole() instanceof ClientRole) {
                                Intent intent = new Intent(getApplicationContext(), ClientHome.class);
                                startActivity(intent);
                            } else if (currentUser.getRole() instanceof CookRole) {
                                Intent intent = new Intent(getApplicationContext(), CookHome.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        }.start();
    }

    private static final String TAG = "LoginActivity";

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

    public void btnDebugMode(View view){
        System.out.println("debug");
        Intent intent = new Intent(getApplicationContext(), DebugMode.class);
        startActivity(intent);
    }




}