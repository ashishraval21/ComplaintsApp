package com.municipal.complaintsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.municipal.complaintsapp.complaint.Complaint;
import com.municipal.complaintsapp.complaint.ComplaintAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


    public class ComplaintListActivity extends AppCompatActivity {
        private List<Complaint> complaints = new ArrayList<>();
        private RecyclerView recyclerView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_complaints_list);
            getSupportActionBar().setTitle("Complaint History");

            // Initialize complaints list
            // Add sample complaints

            complaints.add(new Complaint("Street light not working.", "Pending", new Date(123, 3, 15, 15, 42, 25), "Street light not working from past couple of days."));
//            complaints.add(new Complaint("Printer out of ink", "Resolved", new Date()));
//            complaints.add(new Complaint("Slow internet speed", "Pending", new Date()));
//            complaints.add(new Complaint("Wi-Fi not working", "Pending", new Date()));
//            complaints.add(new Complaint("Printer out of ink", "Resolved", new Date()));
//            complaints.add(new Complaint("Slow internet speed", "Pending", new Date()));
//            complaints.add(new Complaint("Wi-Fi not working", "Pending", new Date()));
//            complaints.add(new Complaint("Printer out of ink", "Resolved", new Date()));
//            complaints.add(new Complaint("Slow internet speed", "Pending", new Date()));
//            complaints.add(new Complaint("Wi-Fi not working", "Pending", new Date()));
//            complaints.add(new Complaint("Printer out of ink", "Resolved", new Date()));
//            complaints.add(new Complaint("Slow internet speed", "Pending", new Date()));    complaints.add(new Complaint("Wi-Fi not working", "Pending", new Date()));
//            complaints.add(new Complaint("Printer out of ink", "Resolved", new Date()));
//            complaints.add(new Complaint("Slow internet speed", "Pending", new Date()));
//            complaints.add(new Complaint("Wi-Fi not working", "Pending", new Date()));
//            complaints.add(new Complaint("Printer out of ink", "Resolved", new Date()));
//            complaints.add(new Complaint("Slow internet speed", "Pending", new Date()));
//            complaints.add(new Complaint("Wi-Fi not working", "Pending", new Date()));
//            complaints.add(new Complaint("Printer out of ink", "Resolved", new Date()));
//            complaints.add(new Complaint("Slow internet speed", "Pending", new Date()));




            // Initialize RecyclerView
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ComplaintAdapter(complaints, this));
        }
    }




