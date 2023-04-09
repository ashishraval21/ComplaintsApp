package com.municipal.complaintsapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonParser;
import com.municipal.complaintsapp.API.APICallUtils;
import com.municipal.complaintsapp.API.ApiList;
import com.municipal.complaintsapp.API.JsonUtils;
import com.municipal.complaintsapp.API.MethodType;
import com.municipal.complaintsapp.R;
import com.municipal.complaintsapp.complaint.Complaint;
import com.municipal.complaintsapp.complaint.ComplaintAdapter;
import com.municipal.complaintsapp.util.BackgroundTask;
import com.municipal.complaintsapp.util.SharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    private ComplaintAdapter complaintAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Initialize RecyclerView
        View fragmentView = inflater.inflate(R.layout.fragment_complaint_list, container, false);
        recyclerView = fragmentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragmentView.getContext()));
        complaintAdapter = new ComplaintAdapter(complaints, fragmentView.getContext());
        callBackgroundTask();
        // Inflate the layout for this fragment
        return fragmentView;
    }

    private void callBackgroundTask() {
        showProgressBar(true);
        BackgroundTask<String> backgroundTask = new BackgroundTask<>(new BackgroundTask.BackgroundTaskExecutor<String>() {
            @Override
            public String run() throws Exception {
                APICallUtils.makeApiCallWithRetry(ApiList.FetchAllComplaints.getApi() + new SharedPreference(getActivity()).getId(),
                        MethodType.GET.name(), new JSONObject(), new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                e.printStackTrace();
                                Snackbar mySnackbar = Snackbar.make(requireView(), e.getMessage(), BaseTransientBottomBar.LENGTH_LONG);
                                mySnackbar.show();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if(response.isSuccessful()){

                                    try{
                                        JSONArray array = new JSONArray(response.body().string());
                                        if(array.length() == 0){

                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    requireActivity().findViewById(R.id.emptyMessageLayout).setVisibility(View.VISIBLE);

                                                    Snackbar mySnackbar = Snackbar.make(requireView(), "No Complaints Found.", BaseTransientBottomBar.LENGTH_LONG);
                                                    mySnackbar.show();
                                                }
                                            });

                                        }else {

                                            for(int  i = 0; i< array.length(); i++){
                                                Complaint complaint = JsonUtils.fromJson((JSONObject) array.get(i), Complaint.class);
                                                complaints.add(complaint);
                                            }

                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    recyclerView.setAdapter(complaintAdapter);
                                                }
                                            });


                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }


                                }
                            }
                        });

                return null;
            }
        }, new BackgroundTask.BackgroundTaskListener<String>() {
            @Override
            public void onSuccess(String result) {
                showProgressBar(false);
            }

            @Override
            public void onFailure(Exception e) {
                showProgressBar(false);
            }
        });

        backgroundTask.execute();

    }

    public void showProgressBar(boolean show) {
        ProgressBar progressBar = requireActivity().findViewById(R.id.progress_bar);
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }


}