package com.uottawa.seg2105.grp1.mealer.ui;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.Complaint;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.storage.IRepository;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

public class ComplainButtonActivity extends AppCompatActivity {

    public static String complaintID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_button);
    }

    public void btnComplain(View view) {
        try {
            new Thread() {
                @Override
                public void run() {
                    try {
                        Complaint c = Complaint.create("This is my complaint", "The food was bad", "client@email.com", "cook@email.com");
                        complaintID = c.getId();
                        System.out.println("ID:::::::::::::::::::::::::::::" + complaintID);
                    } catch (RepositoryRequestException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnArchive(View view) throws RepositoryRequestException {
        IRepository rep = MealerSystem.getSystem().getRepository();
        try {
            new Thread() {
                @Override
                public void run() {
                    Complaint c = null;
                    try {
                        c = rep.getById(Complaint.class, complaintID);
                        c.archive();
                    } catch (RepositoryRequestException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Archived???????????????????????" + c.isArchived());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Archived???????????????????????" + c.isArchived());
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnUnarchiveAll(View view) throws RepositoryRequestException {
        Complaint.unarchiveAll();
    }
}