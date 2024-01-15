package com.example.movieexplorer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieexplorer.R;
import com.example.movieexplorer.models.Movie;

import java.util.List;

public class MovieCarouselAdapter extends RecyclerView.Adapter<MovieCarouselAdapter.ViewHolder> {

    private Context context;
    private List<Movie> models;
    private OnClickListener onClickListener;
    private static final String TAG ="MOVIE_CAROUSEL_ADAPTER";

    public MovieCarouselAdapter(Context context, List<Movie> models) {
        this.context = context;
        this.models = models;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_carousel,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(this.context).load(this.models.get(position).getMovieBannerUri()).into(holder.movieImage);
        holder.movieTitle.setText(models.get(position).getMovieName());
        if (this.onClickListener != null)
            holder.container.setOnClickListener(v -> this.onClickListener.onClick(v,this.models.get(position)));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public interface OnClickListener{
        void onClick(View view, Movie model);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView movieImage;
        private TextView movieTitle;
        private View container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.movieImage = itemView.findViewById(R.id.movie_item_image);
            this.movieTitle = itemView.findViewById(R.id.movie_item_title);
            this.container = itemView.findViewById(R.id.carousel_container);
        }
    }
}
