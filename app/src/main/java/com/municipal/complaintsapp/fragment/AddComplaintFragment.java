package com.municipal.complaintsapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.municipal.complaintsapp.API.APICallUtils;
import com.municipal.complaintsapp.API.ApiList;
import com.municipal.complaintsapp.API.JsonUtils;
import com.municipal.complaintsapp.API.MethodType;
import com.municipal.complaintsapp.ComplaintListActivity;
import com.municipal.complaintsapp.R;
import com.municipal.complaintsapp.classes.AddComplaint;
import com.municipal.complaintsapp.classes.Complaint;
import com.municipal.complaintsapp.util.SharedPreference;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddComplaintFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddComplaintFragment extends Fragment {
    private static final String TAG = "ComplaintActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 1001;
    private static final int REQUEST_IMAGE_CAPTURE = 1002;
    private static final int REQUEST_IMAGE_PICK = 1003;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private EditText mSubjectEditText;

    private EditText mDescriptionEditText;
    private EditText mComplaintEditText;
    private GridView mImageGrid;
    private Button mAddImageButton;
    private Button mSubmitButton;
    private Button mCancelButton;

    private ArrayList<String> mImageList;

    private ArrayList<String> imagePathList;
    private ImageAdapter mImageAdapter;

    private Uri mImageUri;
    private File mPhotoFile;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    protected View view;


    public AddComplaintFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddComplaintFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddComplaintFragment newInstance(String param1, String param2) {
        AddComplaintFragment fragment = new AddComplaintFragment();
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

        View view = inflater.inflate(R.layout.fragment_add_complaint, container, false);
        this.view = view;
        verifyStoragePermissions(getActivity());
        TextInputLayout subjectLayout = view.findViewById(R.id.subject);
        mSubjectEditText =  subjectLayout.getEditText();

        TextInputLayout descriptionLayout = view.findViewById(R.id.description);
        mDescriptionEditText = descriptionLayout.getEditText();

        mImageGrid = view.findViewById(R.id.image_grid);
        mAddImageButton = view.findViewById(R.id.add_image_button);
        mSubmitButton = view.findViewById(R.id.submit_button);
        mCancelButton = view.findViewById(R.id.cancel_button);

        mImageList = new ArrayList<>();
        imagePathList = new ArrayList<>();
        mImageAdapter = new ImageAdapter(view, mImageList);
        mImageGrid.setAdapter(mImageAdapter);

        mAddImageButton.setOnClickListener(view1 -> showImageDialog());

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
                getActivity().finish();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    private void showImageDialog() {

        verifyStoragePermissions(getActivity());

        String[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Add Image");
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
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

        if (! getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(getContext(), "No camera available", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPhotoFile = createImageFile();
        if (mPhotoFile != null) {
            Uri photoUri = FileProvider.getUriForFile(getContext(), getActivity().getPackageName() + ".provider", mPhotoFile);
            mImageUri = photoUri;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
    }


    private void pickImage() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    }

    private void showImagePreview(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageURI(Uri.parse(mImageList.get(position)));
        builder.setView(imageView);
        builder.setPositiveButton("Delete", (dialog, which) -> {
            mImageList.remove(position);
            imagePathList.remove(position);
            notifyDataSet();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


    private void submitComplaint() {
        // Implement complaint submission logic here
        // including image list mImageList
        int UserId = new SharedPreference(getActivity()).getId();
        Complaint complaint = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            complaint = new AddComplaint(UserId, mSubjectEditText.getText().toString(), mDescriptionEditText.getText().toString(),
                    imagePathList);
        }

        APICallUtils.makeApiCallWithRetry(ApiList.AddComplaint.getApi(), MethodType.POST_MULTIPART.toString(), JsonUtils.getJsonObject(complaint), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Snackbar mySnackbar = Snackbar.make(view, e.getMessage(), BaseTransientBottomBar.LENGTH_LONG);
                mySnackbar.show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    Snackbar mySnackbar = Snackbar.make(view, "Complaint Register SuccessFully", BaseTransientBottomBar.LENGTH_LONG);
                    mySnackbar.show();

                }
            }
        });


    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);

        String imagePath = imageFile.getAbsolutePath();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, imagePath);
        getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        System.out.println("Image Path :=> "+imagePath + "  "+imageFile.exists());
        return imageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                 mImageList.add(mPhotoFile.getAbsolutePath());
                imagePathList.add(mPhotoFile.getAbsolutePath());
                notifyDataSet();
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                Uri selectedImageUri = data.getData();
                System.out.println("URI PICK IMAGE :"+selectedImageUri.toString());
                System.out.println("URI PICK IMAGE PATH :"+selectedImageUri.getPath());
                String path = getRealPathFromURI(selectedImageUri);
                mImageList.add(selectedImageUri.toString());
                imagePathList.add(path);
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
                Toast.makeText(getContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == REQUEST_EXTERNAL_STORAGE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    takePhoto();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(getContext(), "Storage permission denied.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void notifyDataSet(){
        mImageAdapter.notifyDataSetChanged();
        if(mImageList.size() == 3){
            getView().findViewById(R.id.add_image_button).setEnabled(Boolean.FALSE);
        }else if(!getView().findViewById(R.id.add_image_button).isEnabled()){
            getView().findViewById(R.id.add_image_button).setEnabled(Boolean.TRUE);
        }
    }


    private static class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<String> mImageList;

        private final View view;


        public ImageAdapter(View view, ArrayList<String> imageList) {
            this.view = view;
            mImageList = imageList;
            mContext = view.getContext();
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
            Uri uri = Uri.parse(mImageList.get(position));

            imageView.setOnClickListener(v -> {

                if(mContext instanceof ComplaintListActivity){
                    ComplaintListActivity mContext1 = (ComplaintListActivity) mContext;
                    AddComplaintFragment addComplaintFragment = (AddComplaintFragment) mContext1.getSupportFragmentManager().findFragmentById(R.id.frameFragment);
                        addComplaintFragment.showImagePreview(position);

                }

            });
            return imageView;
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}