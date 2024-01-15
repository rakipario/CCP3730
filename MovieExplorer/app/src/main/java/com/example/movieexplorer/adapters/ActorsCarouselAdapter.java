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
import com.example.movieexplorer.models.Cast;

import java.util.List;

public class ActorsCarouselAdapter extends RecyclerView.Adapter<ActorsCarouselAdapter.ViewHolder> {
    private Context context;
    private List<Cast> casts;

    public ActorsCarouselAdapter(Context context, List<Cast> casts) {
        this.context = context;
        this.casts = casts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_actor_carousel,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(this.context).load(casts.get(position).getPhotoUri()).into(holder.actorImage);
        holder.actorName.setText(String.format("%s %s",casts.get(position).getName(),casts.get(position).getSurname()));
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView actorImage;
        private TextView actorName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            actorImage = itemView.findViewById(R.id.item_actor_image);
            actorName = itemView.findViewById(R.id.item_actor_name);
        }
    }
}
