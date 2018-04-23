package com.example.wanqianhw9;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


/**
 * Created by anwanqi on 4/21/18.
 */

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {
    private List<SearchResults> favoriteList;
    private Context context;
    private static RecyclerViewClickListener mCallback;
//    private RecyclerView mRecyclerView;


    public FavoriteListAdapter(List<SearchResults> res, final Context context,RecyclerViewClickListener itemListener){
        favoriteList = res;
        this.context = context;
        mCallback = itemListener;
    }

//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//
//        mRecyclerView = recyclerView;
//    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView address;
        public ImageView imgview;
        public ImageView favView;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            name = (TextView) v.findViewById(R.id.result_item_name);
            address = (TextView) v.findViewById(R.id.result_item_address);
            imgview = (ImageView) v.findViewById(R.id.result_item_img);
            favView = (ImageView) v.findViewById(R.id.fav_img);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SearchResults res = favoriteList.get(position);
        holder.name.setText(res.getName());
        holder.address.setText(res.getAddress());
        holder.favView.setImageResource(R.drawable.heart_fill_red);

        if(res.getImgUri() != null){
            final String imageUrl = res.getImgUri();
            holder.imgview.setVisibility(View.VISIBLE);
            new AsyncTask<Void, Void, Bitmap>(){
                @Override
                protected Bitmap doInBackground(Void... params) {
                    return getBitmapFromURL(imageUrl);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    holder.imgview.setImageBitmap(bitmap);
                }
            }.execute();
        } else{
            holder.imgview.setVisibility(View.GONE);
        }

        holder.favView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toastText = res.getName() + " was removed from favorites";
                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
                String keyId = res.getPlace_id();
                SharedPreferenceManager.getInstance(context.getApplicationContext()).removeFavourite(keyId);
                mCallback.recyclerViewListClicked();
            }
        });


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String placeId = res.getPlace_id();
                String placeName = res.getName();
                Double placeLat = res.getPlace_lat();
                Double placeLng = res.getPlace_lng();
                Bundle bundle = new Bundle();
                bundle.putString("PlaceID", placeId);
                bundle.putString("PlaceName",placeName);
                bundle.putDouble("placeLat",placeLat);
                bundle.putDouble("placeLng",placeLng);
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public FavoriteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.result_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public static Bitmap getBitmapFromURL(String imageUrl) {
        Bitmap bitmap = null;

        if (bitmap == null) {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error: ", e.getMessage().toString());
            }
        }

        return bitmap;
    }


    public interface RecyclerViewClickListener{
        public void recyclerViewListClicked();
    }

}
