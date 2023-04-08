package com.municipal.complaintsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.municipal.complaintsapp.API.APICallUtils;
import com.municipal.complaintsapp.API.ApiList;
import com.municipal.complaintsapp.API.JsonUtils;
import com.municipal.complaintsapp.API.MethodType;
import com.municipal.complaintsapp.Adapter.ImageAdapter;
import com.municipal.complaintsapp.complaint.Attachment;
import com.municipal.complaintsapp.complaint.Complaint;
import com.municipal.complaintsapp.util.DateUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ComplaintDetailsView extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1003;
    private Complaint complaint;

    private GridView mImageGrid;
    private ImageAdapter mImageAdapter;
    List<String> images;

    public void showProgressBar(boolean show) {
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_complaint_details_view);
        showProgressBar(true);
        images = new ArrayList<>();
        complaint = (Complaint) getIntent().getSerializableExtra("complaint");
        StringBuilder builderZeroes = new StringBuilder();
        int id = complaint.getId();
        if(id < 1000){
            builderZeroes.append('0');
            if(id < 100){
                builderZeroes.append('0');
            }
            if(id < 10){
                builderZeroes.append('0');
            }
        }
        TextView status = findViewById(R.id.complaint_details_status);
        status.setText(complaint.getStatus());

        getSupportActionBar().setTitle("Complaint Id : "+ builderZeroes + id);

                APICallUtils.makeApiCallWithRetry(ApiList.FetchComplaintById.getApi() + complaint.getId(),
                        MethodType.GET.name(), new JSONObject(), new Callback() {


                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                e.printStackTrace();
                                View rootView = findViewById(android.R.id.content);
                                Snackbar.make(rootView, e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if(response.isSuccessful()){
                                    try {
                                        String body =  response.body().string();

                                       Complaint complaint =  JsonUtils.fromJsonWithDate(new JSONObject(body), Complaint.class);
                                        View rootView = findViewById(android.R.id.content);

                                               runOnUiThread(new Runnable() {
                                                   @Override
                                                   public void run() {
                                                       // Update the UI with the complaint details
                                                       TextView title = rootView.findViewById(R.id.complaint_details_title);
                                                       title.setText(complaint.getTitle());

                                                       TextView date = rootView.findViewById(R.id.complaint_details_date);
                                                       date.setText(DateUtils.getDateTimeString( complaint.getDate()));

                                                       TextView description = rootView.findViewById(R.id.complaint_details_description);
                                                       description.setText(complaint.getDescription());


                                                       for(Attachment attachment : complaint.getAttachments()){
                                                           images.add(attachment.getFilePath());
                                                       }

                                                       mImageGrid = rootView.findViewById(R.id.complaintsDetailsViewGrid);
                                                       mImageAdapter = new ImageAdapter(ComplaintDetailsView.this, images);
                                                       mImageGrid.setAdapter(mImageAdapter);
                                                   }
                                               });



                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });


        showProgressBar(false);

    }



//    private class ImageAdapter extends BaseAdapter {
//        private Context mContext;
//        private List<String> mImageList;
//
//        HashMap<Integer,String> hashMap = new HashMap<>();
//
//        private Set<String> imageSet = new HashSet<>();
//
//
//        public ImageAdapter(Context context, List<String> mImageList){
//            mContext = context;
//            this.mImageList = mImageList;
//        }
//
//        @Override
//        public int getCount() {
//            return mImageList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mImageList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ImageView imageView = new ImageView(mContext);
//                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//            Toast.makeText(mContext, position +" "+mImageList.size() +" "+ mImageList.get(position), Toast.LENGTH_LONG).show();
//
//            String imageUrl = mImageList.get(position);
//            Picasso.get()
//                    .load(imageUrl)
//                    .into(imageView);
//
//
//            return imageView;
//        }
//    }




}