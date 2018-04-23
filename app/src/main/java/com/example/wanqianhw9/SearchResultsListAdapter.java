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

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by anwanqi on 4/13/18.
 */

public class SearchResultsListAdapter extends RecyclerView.Adapter<SearchResultsListAdapter.ViewHolder> {
    private List<SearchResults> resultList;
    private Context context;


    public SearchResultsListAdapter(List<SearchResults> res, final Context context){
        resultList = res;
        this.context = context;
    }

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
        final SearchResults res = resultList.get(position);
        holder.name.setText(res.getName());
        holder.address.setText(res.getAddress());

        if(SharedPreferenceManager.getInstance(context.getApplicationContext()).isFavourite(res.getPlace_id())){
            holder.favView.setImageResource(R.drawable.heart_fill_red);
        } else{
            holder.favView.setImageResource(R.drawable.heart_outline_black);
        }

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
                String toastText = res.getName();
                String keyId = res.getPlace_id();
                if(SharedPreferenceManager.getInstance(context.getApplicationContext()).isFavourite(keyId)){
                    toastText += " was removed from favorites";
                    holder.favView.setImageResource(R.drawable.heart_outline_black);
                    SharedPreferenceManager.getInstance(context.getApplicationContext()).removeFavourite(keyId);
                } else{
                    toastText += " was added to favorites";
                    holder.favView.setImageResource(R.drawable.heart_fill_red);
                    SearchResults newOne = new SearchResults(res.getImgUri(),res.getName(),res.getAddress(),res.getPlace_id(), res.getPlace_lat(),res.getPlace_lng());
                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(newOne);
                    SharedPreferenceManager.getInstance(context.getApplicationContext()).setFavourite(res.getPlace_id(),jsonInString);
                }
                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
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
                bundle.putString("placeImgUrl",res.getImgUri());
                bundle.putString("placeAddress",res.getAddress());
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public SearchResultsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.result_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCount() {
        return resultList.size();
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


}
