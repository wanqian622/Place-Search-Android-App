package com.example.wanqianhw9;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

/**
 * Created by anwanqi on 4/18/18.
 */

public class PhotosListAdapter extends RecyclerView.Adapter<PhotosListAdapter.ViewHolder> {
//    private List<Bitmap> photoList;
//    private Bitmap photoBitmap;
   private List<PlacePhotoMetadata> photosList;
    private Context context;
    private GeoDataClient mGeoDataClient;

    //List<Bitmap> res
    public PhotosListAdapter(List<PlacePhotoMetadata> res, final Context context){
        photosList = res;
        this.context = context;
        mGeoDataClient = Places.getGeoDataClient(context, null);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgview;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            imgview = (ImageView) v.findViewById(R.id.photo_item_img);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(photosList.get(position) != null){
            Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photosList.get(position));
            photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                    PlacePhotoResponse photo = task.getResult();
                    Bitmap photoBitmap = photo.getBitmap();
                    holder.imgview.setVisibility(View.VISIBLE);
                    holder.imgview.setImageBitmap(photoBitmap);
                }
            });

        } else{
            holder.imgview.setVisibility(View.GONE);
        }
    }


    @Override
    public PhotosListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.photo_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCount() {
        return photosList.size();

    }

}
