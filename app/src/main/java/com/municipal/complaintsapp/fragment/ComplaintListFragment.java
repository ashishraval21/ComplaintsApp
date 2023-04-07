package com.municipal.complaintsapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.municipal.complaintsapp.R;
import com.municipal.complaintsapp.complaint.Complaint;
import com.municipal.complaintsapp.complaint.ComplaintAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComplaintListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComplaintListFragment extends Fragment {

    private List<Complaint> complaints = new ArrayList<>();
    private RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ComplaintListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComplaintListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComplaintListFragment newInstance(String param1, String param2) {
        ComplaintListFragment fragment = new ComplaintListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize RecyclerView
        View fragmentView = inflater.inflate(R.layout.fragment_complaint_list, container, false);

        recyclerView = fragmentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ComplaintAdapter(complaints, getContext()));
        // Inflate the layout for this fragment
        return fragmentView;
    }
}