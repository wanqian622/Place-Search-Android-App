package com.example.wanqianhw9;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.example.wanqianhw9.SearchResultsListAdapter.getBitmapFromURL;

/**
 * Created by anwanqi on 4/19/18.
 */

public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.ViewHolder>{
    private List<Reviews> reviews;
    private Context context;

    public ReviewsListAdapter(List<Reviews> reviews, final Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView time;
        public TextView description;
        public RatingBar rating;
        public ImageView profileView;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            profileView = (ImageView) v.findViewById(R.id.review_item_img);
            name = (TextView) v.findViewById(R.id.reviewerName);
            rating = (RatingBar) v.findViewById(R.id.review_ratingBar);
            time = (TextView) v.findViewById(R.id.reviewTime);
            description = (TextView) v.findViewById(R.id.review_description);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Reviews res = reviews.get(position);
        holder.name.setText(res.getAuthor());
        holder.description.setText(res.getDescription());
        holder.rating.setRating((float)res.getRate());
        holder.time.setText(res.getReview_time());

        if(res.getProfileUrl() != null){
            final String imageUrl = res.getProfileUrl();
            holder.profileView.setVisibility(View.VISIBLE);
            new AsyncTask<Void, Void, Bitmap>(){
                @Override
                protected Bitmap doInBackground(Void... params) {
                    return getBitmapFromURL(imageUrl);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    holder.profileView.setImageBitmap(bitmap);
                }
            }.execute();
        } else{
            holder.profileView.setVisibility(View.GONE);
        }

//        holder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String placeId = res.getPlace_id();
//                String placeName = res.getName();
//                Double placeLat = res.getPlace_lat();
//                Double placeLng = res.getPlace_lng();
//                Bundle bundle = new Bundle();
//                bundle.putString("PlaceID", placeId);
//                bundle.putString("PlaceName",placeName);
//                bundle.putDouble("placeLat",placeLat);
//                bundle.putDouble("placeLng",placeLng);
//                Intent intent = new Intent(context, DetailsActivity.class);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });
    }
    @Override
    public ReviewsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.review_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCount() {
        return reviews.size();
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
