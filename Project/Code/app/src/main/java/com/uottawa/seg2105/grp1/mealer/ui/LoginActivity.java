package com.uottawa.seg2105.grp1.mealer.ui;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.BuildConfig;
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

        // Disable the debug menu if not in debug mode
        if (!BuildConfig.DEBUG) {
            Button debugBtn = (Button) findViewById(R.id.btnDebug);
            debugBtn.setVisibility(View.GONE); // Allow other components to take the button's space
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Temporarily disable all buttons while checking the user
        View btnLogin = findViewById(R.id.btnLogin);
        View btnRegisterClient = findViewById(R.id.btnRegisterClient);
        View btnRegisterCook = findViewById(R.id.btnRegisterCook);

        btnLogin.setEnabled(false);
        btnRegisterCook.setEnabled(false);
        btnRegisterClient.setEnabled(false);

        new Thread() {
            @Override
            public void run() {
                MealerSystem system = MealerSystem.getSystem();
                system.restoreSession();

                User currentUser = system.getCurrentUser();

                if (currentUser == null) {
                    runOnUiThread(() -> {
                        btnLogin.setEnabled(true);
                        btnRegisterCook.setEnabled(true);
                        btnRegisterClient.setEnabled(true);
                    });
                    return;
                }

                runOnUiThread(() -> {
                    if (currentUser.isAdmin()) {
                        Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                        startActivity(intent);
                    } else {
                        if (currentUser.getRole() instanceof ClientRole) {
                            Intent intent = new Intent(getApplicationContext(), ClientHome.class);
                            startActivity(intent);
                        } else if (currentUser.getRole() instanceof CookRole) {
                            // Check if the cook has been banned
                            CookRole role = (CookRole) currentUser.getRole();
                            long banExpirationMillis = role.getBanExpiration();

                            // TODO: Send banned user to SuspendedHome
                            if (banExpirationMillis == 0) {
                                Intent intent = new Intent(getApplicationContext(), SuspensionHome.class);
                                intent.putExtra("banExpiration", 0);
                                startActivityForResult(intent, 0);

                                btnLogin.setEnabled(true);
                                btnRegisterCook.setEnabled(true);
                                btnRegisterClient.setEnabled(true);

                                return;
                            } else if (banExpirationMillis > 0) {
                                // This will be vulnerable to the users changing their phone's clock time but checking times form the internet seems a bit out of scope for this project.
                                long currentTimeMillis = System.currentTimeMillis();

                                System.out.println(String.format(
                                        "Current: %d, banExpiration: %d",
                                        currentTimeMillis,
                                        banExpirationMillis
                                ));

                                if (banExpirationMillis >= currentTimeMillis) {
                                    Intent intent = new Intent(getApplicationContext(), SuspensionHome.class);
                                    intent.putExtra("banExpiration", banExpirationMillis - currentTimeMillis);
                                    startActivityForResult(intent, 0);

                                    btnLogin.setEnabled(true);
                                    btnRegisterCook.setEnabled(true);
                                    btnRegisterClient.setEnabled(true);

                                    return;
                                }
                            }

                            Intent intent = new Intent(getApplicationContext(), CookHome.class);
                            startActivity(intent);
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