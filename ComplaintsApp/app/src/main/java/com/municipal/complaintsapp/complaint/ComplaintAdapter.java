package com.municipal.complaintsapp.complaint;

import com.municipal.complaintsapp.ComplaintDetailsView;
import com.municipal.complaintsapp.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder> {
    private List<Complaint> complaints;
    private Context context;

    public ComplaintAdapter(List<Complaint> complaints, Context context) {
        this.complaints = complaints;
        this.context = context;
    }

    public ComplaintAdapter(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    @Override
    public ComplaintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_complaints_list_item, parent, false);
        return new ComplaintViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ComplaintViewHolder holder, int position) {
        Complaint complaint = complaints.get(position);
        holder.titleTextView.setText(complaint.getTitle());
        holder.statusTextView.setText(complaint.getStatus());
        holder.dateTextView.setText(android.text.format.DateFormat.format("dd/MM/yyyy", complaint.getDate()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the ComplaintDetailsActivity when a complaint is clicked
                Intent intent = new Intent(context, ComplaintDetailsView.class);
                intent.putExtra("complaint", complaint);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }

    public static class ComplaintViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView statusTextView;
        public TextView dateTextView;

        public CardView cardView;


        public ComplaintViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textTitle);
            statusTextView = itemView.findViewById(R.id.textStatus);
            dateTextView = itemView.findViewById(R.id.textDate);
            cardView = (CardView) itemView;

        }
    }
}
