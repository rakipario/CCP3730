package com.example.movieexplorer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieexplorer.R;
import com.example.movieexplorer.models.Movie;

import java.util.List;

public class BannerCarouselAdapter extends RecyclerView.Adapter<BannerCarouselAdapter.ViewHolder>{
    private Context context;
    private List<Movie> movies;

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public BannerCarouselAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_banner,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(this.context).load(this.movies.get(position).getMovieBannerUri()).into(holder.imageView);
        if (this.onClickListener != null)
            holder.imageView.setOnClickListener(v -> this.onClickListener.onClick(v,this.movies.get(position)));
    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.banner_image);
        }
    }

    public interface OnClickListener{
        void onClick(View view, Movie movie);
    }
}
