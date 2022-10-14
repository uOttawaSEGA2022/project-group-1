package com.uottawa.seg2105.grp1.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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