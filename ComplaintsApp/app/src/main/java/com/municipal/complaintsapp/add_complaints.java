package com.municipal.complaintsapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class add_complaints extends AppCompatActivity {
    private static final String TAG = "ComplaintActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 1001;
    private static final int REQUEST_IMAGE_CAPTURE = 1002;
    private static final int REQUEST_IMAGE_PICK = 1003;

    private EditText mSubjectEditText;

    private EditText mDescriptionEditText;
    private EditText mComplaintEditText;
    private GridView mImageGrid;
    private Button mAddImageButton;
    private Button mSubmitButton;
    private Button mCancelButton;

    private ArrayList<String> mImageList;
    private ImageAdapter mImageAdapter;

    private Uri mImageUri;
    private File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaints);
        getSupportActionBar().setTitle("Register Your Complaint");

        TextInputLayout subjectLayout = findViewById(R.id.subject);
        mSubjectEditText =  subjectLayout.getEditText();

        TextInputLayout descriptionLayout = findViewById(R.id.description);
        mDescriptionEditText = descriptionLayout.getEditText();

        mImageGrid = findViewById(R.id.image_grid);
        mAddImageButton = findViewById(R.id.add_image_button);
        mSubmitButton = findViewById(R.id.submit_button);
        mCancelButton = findViewById(R.id.cancel_button);

        mImageList = new ArrayList<>();
        mImageAdapter = new ImageAdapter(this, mImageList);
        mImageGrid.setAdapter(mImageAdapter);

        mAddImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog();
            }
        });

        mImageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showImagePreview(i);
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitComplaint();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showImageDialog() {
        String[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Add Image");
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    } else {
                        try {
                            takePhoto();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 1:
                    pickImage();
                    break;
                case 2:
                    dialog.dismiss();
                    break;
            }
        });
        builder.show();
    }

    private void takePhoto() throws IOException {

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera available", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         mPhotoFile = createImageFile();
            if (mPhotoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", mPhotoFile);
                mImageUri = photoUri;
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
    }


    private void pickImage() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    }

    private void showImagePreview(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ImageView imageView = new ImageView(this);
        imageView.setImageURI(Uri.parse(mImageList.get(position)));
        builder.setView(imageView);
        builder.setPositiveButton("Delete", (dialog, which) -> {
            mImageList.remove(position);
            notifyDataSet();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void submitComplaint() {
        // Implement complaint submission logic here
        // including image list mImageList
        finish();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                mImageList.add(mPhotoFile.getAbsolutePath());
                notifyDataSet();
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                Uri selectedImageUri = data.getData();
                mImageList.add(selectedImageUri.toString());
                notifyDataSet();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    takePhoto();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void notifyDataSet(){
        mImageAdapter.notifyDataSetChanged();
        if(mImageList.size() == 3){
            findViewById(R.id.add_image_button).setEnabled(Boolean.FALSE);
        }else if(!findViewById(R.id.add_image_button).isEnabled()){
            findViewById(R.id.add_image_button).setEnabled(Boolean.TRUE);
        }
    }

    private static class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<String> mImageList;

        public ImageAdapter(Context context, ArrayList<String> imageList) {
            mContext = context;
            mImageList = imageList;
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public Object getItem(int position) {
            return mImageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageURI(Uri.parse(mImageList.get(position)));
            imageView.setOnClickListener(v -> {
                if (mContext instanceof add_complaints) {
                    ((add_complaints) mContext).showImagePreview(position);
                }
            });
            return imageView;
        }
    }
}

