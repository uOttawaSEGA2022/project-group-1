package com.uottawa.seg2105.grp1.mealer.ui;

// TODO: Create other Activities (see classes_explanation.md)

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.grp1.mealer.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

}