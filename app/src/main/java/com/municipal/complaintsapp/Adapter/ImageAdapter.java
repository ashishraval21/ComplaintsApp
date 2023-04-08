package com.municipal.complaintsapp.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> imageList;



    public ImageAdapter(Context context, List<String> mImageList){
        mContext = context;
        this.imageList = mImageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Toast.makeText(mContext, position +" "+imageList.size() +" "+ imageList.get(position), Toast.LENGTH_LONG).show();

        String imageUrl = imageList.get(position);
        Picasso.get()
                .load(imageUrl)
                .into(imageView);


        return imageView;
    }
}
