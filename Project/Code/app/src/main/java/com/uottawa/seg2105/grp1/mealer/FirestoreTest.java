package com.uottawa.seg2105.grp1.mealer;

import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import androidx.annotation.NonNull;

public class FirestoreTest {

    private static final String TAG = "FirestoreTest";

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private final FirebaseFirestore db;

    FirestoreTest(FirebaseFirestore db) {
        this.db = db;

    }

    public void addUser(View view) {
        // Create a new user with a first and last name

        //TODO:
        // extract data entered from fields &
        // make sure email, password & role are valid
        //Data is set explicitly for testing purposes

        Map<String, Object> user = new HashMap<>();
        user.put("email", "henry.c@email.com");
        user.put("password", "123");
        user.put("role", "cook");

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                //Error checking
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
