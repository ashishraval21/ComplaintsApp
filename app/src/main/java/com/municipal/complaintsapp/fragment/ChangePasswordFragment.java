package com.municipal.complaintsapp.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.municipal.complaintsapp.API.APICallUtils;
import com.municipal.complaintsapp.API.ApiList;
import com.municipal.complaintsapp.API.JsonUtils;
import com.municipal.complaintsapp.API.MethodType;
import com.municipal.complaintsapp.R;
import com.municipal.complaintsapp.util.BackgroundTask;
import com.municipal.complaintsapp.util.CustomAlertUtils;
import com.municipal.complaintsapp.util.PasswordValidator;
import com.municipal.complaintsapp.util.SharedPreference;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmPassword;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        currentPassword = view.findViewById(R.id.currentPassword);
        newPassword =  view.findViewById(R.id.newPassword);
        confirmPassword =  view.findViewById(R.id.confirmPassword);


        PasswordValidator.addPasswordValidator(currentPassword);
        PasswordValidator.addPasswordValidator(newPassword);
        PasswordValidator.addPasswordValidator(confirmPassword);


        Button submitButton =  view.findViewById(R.id.submitChangeButton);
        submitButton.setOnClickListener(view1 -> {
            // Perform validation checks for the input passwords
            if (isValidPassword(currentPassword.getText().toString(), newPassword.getText().toString(), confirmPassword.getText().toString())) {
                updatePassword(currentPassword.getText().toString(), newPassword.getText().toString(), view1);
            }
        });

        Button resetButton = view.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear the text fields
                currentPassword.setText("");
                newPassword.setText("");
                confirmPassword.setText("");
            }
        });

         return view;
    }



    private boolean isValidPassword(String currentPasswordText, String newPasswordText, String confirmPasswordText) {

        if(TextUtils.isEmpty(currentPasswordText)){
            currentPassword.setError("Please enter your current password");
            return false;
        }

        if(TextUtils.isEmpty(newPasswordText)){
            newPassword.setError("Please enter your current password");
            return false;
        }

        if(TextUtils.isEmpty(confirmPasswordText)){
             confirmPassword.setError("Please enter confirm password.");
             return false;
        }

        if(! newPasswordText.equals(confirmPasswordText)){
            confirmPassword.setError("New and Confirm password are mismatched.");
            return false;
        }

        return true;
    }


    private void updatePassword(String currentPassword, String newPassword, View view) {
        int id = new SharedPreference(getActivity()).getId();
        com.municipal.complaintsapp.classes.ChangePassword password = new com.municipal.complaintsapp.classes.ChangePassword(id, currentPassword,newPassword);


        BackgroundTask<String> bg = new BackgroundTask<>(() -> {
            APICallUtils.makeApiCallWithRetry(ApiList.ChangePassword.getApi(), MethodType.PUT.toString(),
                    JsonUtils.getJsonObject(password),  new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if(response.isSuccessful()){

                                try{
                                    String body = response.body().string();

                                    boolean result = Boolean.parseBoolean(body);
                                    String message = result ? "Password Changed SuccessFully" : "Current Password Mismatched";

                                    getActivity().runOnUiThread(() -> new CustomAlertUtils(view1 -> {

                                    },getActivity().findViewById(R.id.layoutDialogContainer) ).showCustomAlertDialog(message, requireContext()));


                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }


                                Snackbar mySnackbar = Snackbar.make(view, "Password Changed SuccessFully", BaseTransientBottomBar.LENGTH_LONG);
                                mySnackbar.show();

                            }else{

                                getActivity().runOnUiThread(() -> new CustomAlertUtils(view1 -> {

                                },getActivity().findViewById(R.id.layoutDialogContainer) ). showCustomAlertDialog("Something went wrong, Please try again", requireContext()));

                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(() -> new CustomAlertUtils(view1 -> {

                            },getActivity().findViewById(R.id.layoutDialogContainer) ). showCustomAlertDialog("Current password mismatched.", requireContext()));

                        }
                    });
            return null;
        }, new BackgroundTask.BackgroundTaskListener<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        bg.execute();


    }

}