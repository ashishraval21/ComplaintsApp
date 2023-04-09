package com.municipal.complaintsapp.fragment;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.municipal.complaintsapp.API.APICallUtils;
import com.municipal.complaintsapp.API.ApiList;
import com.municipal.complaintsapp.API.JsonUtils;
import com.municipal.complaintsapp.API.MethodType;
import com.municipal.complaintsapp.R;
import com.municipal.complaintsapp.classes.Registration;
import com.municipal.complaintsapp.util.BackgroundTask;
import com.municipal.complaintsapp.util.CustomAlertUtils;
import com.municipal.complaintsapp.util.SharedPreference;
import com.municipal.complaintsapp.util.TextBoxValidator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    EditText fullName;

    RadioGroup genderRadioGroup;
    EditText mobile;
    EditText emailNo;
    
    Button update;
    
    Button cancel;

    TextView genderErrorTextView;



    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment user_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_profile, container, false);
        Registration registration = null;
        try {
           String userObject =  new SharedPreference(requireContext()).getUserObject();

           if(! (null == userObject || userObject.isEmpty())){
               registration = JsonUtils.fromJson(new JSONObject(), Registration.class);
           }


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



        fullName = view.findViewById(R.id.nameEdittext);
        emailNo = view.findViewById(R.id.emailEditText);
        mobile = view.findViewById(R.id.mobileEdittext);
        genderRadioGroup = view.findViewById(R.id.gender_radio_group);
        genderErrorTextView= view.findViewById(R.id.gender_error_text_view);

        update = view.findViewById(R.id.btnUpdateUserDetails);
        cancel = view.findViewById(R.id.cancelUpdateDetails);


        TextBoxValidator.inputValidator(fullName);
        TextBoxValidator.inputValidator(emailNo);
        TextBoxValidator.inputValidator(mobile);

        if(registration != null){
            if(registration.getFirstName() != null){
                fullName.setText(registration.getFirstName());
            }

            if(registration.getEmail() != null){
                emailNo.setText(registration.getEmail());
            }

            if(registration.getMobileNo() != null){
                mobile.setText(registration.getMobileNo());
            }

            if(registration.getGender() != null){

                for (int i = 0; i < genderRadioGroup.getChildCount(); i++) {
                    View radioButton = genderRadioGroup.getChildAt(i);
                    if (radioButton instanceof RadioButton) {
                        RadioButton currentRadioButton = (RadioButton) radioButton;
                        String currentText = currentRadioButton.getText().toString();
                        if (currentText.equals(registration.getGender())) {
                            // Set the checked state of the matching RadioButton
                            currentRadioButton.setChecked(true);
                            break;
                        }
                    }
                }

            }

        }
        
        
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetails();
            }
        });
        
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDetails();
            }
        });



        return view;

    }

    private void cancelDetails() {
    }

    private void updateDetails() {

        if(validateDetails()){
            BackgroundTask<String> updateDetailsBgTask = new BackgroundTask<>(new BackgroundTask.BackgroundTaskExecutor<String>() {
                @Override
                public String run() throws Exception {

                    Registration registration = new Registration();
                    registration.setId(new SharedPreference(requireContext()).getId());
                    registration.setMobileNo(mobile.getText().toString());
                    registration.setFirstName(fullName.getText().toString());
                    registration.setEmail(emailNo.getText().toString());
                    int selectedId = genderRadioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = getActivity().findViewById(selectedId);
                    String selectedGender = selectedRadioButton.getText().toString();
                    registration.setGender(selectedGender);


                    APICallUtils.makeApiCallWithRetry(ApiList.UpadateUserDetails.getApi(), MethodType.POST.toString(), JsonUtils.getJsonObject(registration), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                            Snackbar mySnackbar = Snackbar.make(getView(), e.getMessage(), BaseTransientBottomBar.LENGTH_LONG);
                            mySnackbar.show();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                try {
                                    new SharedPreference(getActivity()).createSharedPreference(JsonUtils.getJsonObject(registration));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                getActivity().runOnUiThread(() -> new CustomAlertUtils(view1 -> {

                                },getActivity().findViewById(R.id.layoutDialogContainer) ). showCustomAlertDialog("User Details Updated SuccessFully", requireContext()));

                            }
                        }
                    });



                    return null;
                }
            }, new BackgroundTask.BackgroundTaskListener<String>() {
                @Override
                public void onSuccess(String result) {

                }

                @Override
                public void onFailure(Exception e) {

                }
            });

            updateDetailsBgTask.execute();
        }




    }

    private boolean validateDetails() {

        if(TextUtils.isEmpty(fullName.getText().toString())){
            fullName.setError("Please enter Full Name");
            return false;
        }else{
            fullName.setError(null);
        }

        if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
            genderErrorTextView.setText("Please select a gender");
            genderErrorTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            genderErrorTextView.setVisibility(View.GONE);
        }



        if(TextUtils.isEmpty(mobile.getText().toString())){
            mobile.setError("Please enter MobileNo");
            return false;
        }else{
            mobile.setError(null);
        }

        if(TextUtils.isEmpty(emailNo.getText().toString())){
            emailNo.setError("Please enter Email Id");
            return false;
        }else{
            emailNo.setError(null);
        }



        return true;
    }

}