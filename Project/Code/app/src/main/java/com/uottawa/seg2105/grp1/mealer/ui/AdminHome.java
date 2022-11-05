package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.uottawa.seg2105.grp1.mealer.R;
import com.uottawa.seg2105.grp1.mealer.model.Complaint;
import com.uottawa.seg2105.grp1.mealer.model.MealerSystem;
import com.uottawa.seg2105.grp1.mealer.model.User;
import com.uottawa.seg2105.grp1.mealer.storage.RepositoryRequestException;

import java.util.ArrayList;
import java.util.List;

public class AdminHome extends AppCompatActivity {

    /**
     * A ViewHolder for complaint summaries in the admin complaint inbox
     */
    static public class ComplaintSummaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleView, cookNameView, cookEmailView, clientNameView, clientEmailView;

        public ComplaintSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.complaintSummaryTitle);
            cookNameView = itemView.findViewById(R.id.complaintSummaryCookName);
            cookEmailView = itemView.findViewById(R.id.complaintSummaryCookEmail);
            clientNameView = itemView.findViewById(R.id.complaintSummaryClientName);
            clientEmailView = itemView.findViewById(R.id.complaintSummaryClientEmail);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AdminHome menu = ((ComplaintSummaryAdapter)getBindingAdapter()).getActivity();

            int position = getBindingAdapterPosition();
            if (position == RecyclerView.NO_POSITION)
                return;
            menu.onComplaintClick(getBindingAdapterPosition());
        }
    }

    /**
     * A complaint summary adapter for the admin complaint inbox
     */
    public class ComplaintSummaryAdapter extends RecyclerView.Adapter<ComplaintSummaryViewHolder> {

        Context context;
        List<Complaint> complaints;

        protected ComplaintSummaryAdapter(Context context) {
            this.context = context;
            this.complaints = new ArrayList<Complaint>();
        }

        protected void setComplaints(List<Complaint> complaints) {
            this.complaints = complaints;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ComplaintSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ComplaintSummaryViewHolder(LayoutInflater.from(context).inflate(R.layout.view_complaint_adminhome, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ComplaintSummaryViewHolder holder, int position) {
            Complaint complaint = complaints.get(position);
            User client = complaint.getClient();
            User cook = complaint.getCook();

            holder.titleView.setText(complaint.getTitle());
            holder.cookNameView.setText(cook.getLastName() + ", " + cook.getFirstName());
            holder.cookEmailView.setText(cook.getEmail());
            holder.clientNameView.setText(client.getLastName() + ", " + client.getFirstName());
            holder.clientEmailView.setText(client.getEmail());
        }

        @Override
        public int getItemCount() {
            return complaints.size();
        }

        protected AdminHome getActivity() {
            return AdminHome.this;
        }
    }

    // AdminHome methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        RecyclerView recyclerView = findViewById(R.id.complaintRecyclerView);
        ComplaintSummaryAdapter adapter = new ComplaintSummaryAdapter(getApplicationContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.complaintRecyclerView);

        new Thread() {
            @Override
            public void run() {

                try {
                    List<Complaint> complaints = MealerSystem.getSystem().getRepository().query(
                            Complaint.class,
                            (c) -> !c.isArchived()
                    );

                    runOnUiThread(() -> {
                        ComplaintSummaryAdapter adapter = (ComplaintSummaryAdapter) recyclerView.getAdapter();
                        adapter.setComplaints(complaints);
                    });

                } catch (RepositoryRequestException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Could not fetch data from repository.", Toast.LENGTH_LONG).show());
                }
            }
        }.start();
    }

    /**
     * Takes in a position in the ComplaintSummaryAdapter's complaints list
     * Starts ComplaintActivity and passes the repository ID of the selected complaint to it.
     *
     * @param position a positive index into the complaints list.
     */
    protected void onComplaintClick(int position) {
        RecyclerView recyclerView = findViewById(R.id.complaintRecyclerView);
        ComplaintSummaryAdapter adapter = (ComplaintSummaryAdapter) recyclerView.getAdapter();
        try {
            Complaint complaint = adapter.complaints.get(position);

            if (complaint == null)
                return;

            // Call ComplaintActivity with the complaint
            Intent complaintActivityIntent = new Intent(getApplicationContext(), ComplaintActivity.class);
            complaintActivityIntent.putExtra("complaintId", complaint.getId());
            startActivity(complaintActivityIntent);

        } catch (Exception e) {} // In case the adapter wasn't done updating, this will ignore the click

    }

    public void onLogOff(View view) {
        new Thread() {
            @Override
            public void run() {
                MealerSystem.getSystem().logoff();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        }.start();
    }
}