package com.municipal.complaintsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.municipal.complaintsapp.complaint.Complaint;
import com.municipal.complaintsapp.util.DateUtils;

import java.io.IOException;

public class ComplaintDetailsView extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1003;
    private Complaint complaint;

    ImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_details_view);

        getSupportActionBar().setTitle("Complaint Id : C002102 ");
        complaint = (Complaint) getIntent().getSerializableExtra("complaint");

        // Update the UI with the complaint details
        TextView title = findViewById(R.id.complaint_details_title);
        title.setText(complaint.getTitle());

        TextView status = findViewById(R.id.complaint_details_status);
        status.setText(complaint.getStatus());

        TextView date = findViewById(R.id.complaint_details_date);
        date.setText(DateUtils.getDateTimeString( complaint.getDate()));

        TextView description = findViewById(R.id.complaint_details_description);
        description.setText(complaint.getDescription());


         view = findViewById(R.id.imagei);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
          if (requestCode == REQUEST_IMAGE_PICK) {
                Uri selectedImageUri = data.getData();
                view.setImageURI(selectedImageUri);

            }
        }
    }



}