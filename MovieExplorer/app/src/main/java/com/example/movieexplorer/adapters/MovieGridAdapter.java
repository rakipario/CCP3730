package com.example.movieexplorer.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.movieexplorer.R;
import com.example.movieexplorer.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieGridAdapter extends ArrayAdapter<Movie> {
    private static final String TAG = "MOVIE_GRID_ADAPTER";
    private Context context;
    private List<Movie> movies;
    private View.OnClickListener onClickListener;
    public MovieGridAdapter(@NonNull Context context, @NonNull List<Movie> objects) {
        super(context, 0, objects);
        this.movies = objects;
        this.context = context;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_movie_carousel,parent,false);
        Movie movie = movies.get(position);
        ImageView movieImageView = convertView.findViewById(R.id.movie_item_image);
        TextView movieTextView = convertView.findViewById(R.id.movie_item_title);
        View contentRootView = convertView.getRootView();
        contentRootView.setOnClickListener(this.onClickListener);
        Glide.with(context).load(movie.getMovieBannerUri()).into(movieImageView);
        movieTextView.setText(movie.getMovieName());
        Log.i(TAG, "getView: Grid view must be loaded");
        return convertView;
    }
}
