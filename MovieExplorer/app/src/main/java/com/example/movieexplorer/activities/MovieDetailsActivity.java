package com.example.movieexplorer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.movieexplorer.R;
import com.example.movieexplorer.adapters.ActorsCarouselAdapter;
import com.example.movieexplorer.adapters.TextItemCarouselAdapter;
import com.example.movieexplorer.databinding.ActivityMovieDetailsBinding;
import com.example.movieexplorer.models.Movie;
import com.example.movieexplorer.repositories.MovieRepository;
import com.example.movieexplorer.repositories.Repository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;


public class MovieDetailsActivity extends AppCompatActivity {
    public static String SHARED_MOVIES_NAME = "favorite_movies";
    public static final String ARG_MOVIE_ID = "movie_id";
    private static final String TAG = "ACTIVITY_MOVIE_DETAILS";
    private Movie movie;
    private ActorsCarouselAdapter actorsCarouselAdapter;
    private TextItemCarouselAdapter categoriesCarouselAdapter;
    private ActivityMovieDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent intent = this.getIntent();
        int id  = intent.getIntExtra(ARG_MOVIE_ID,-1);
        Repository<Movie> repository = new MovieRepository();
        Optional<Movie> optionalMovie = repository.findOneById(id);
        if (!optionalMovie.isPresent()){
            Toast.makeText(this,R.string.couldn_t_fetch_movie,Toast.LENGTH_LONG).show();
            finish();
        }
        movie = optionalMovie.get();
        Glide.with(getApplicationContext()).load(movie.getMovieBannerUri()).into(binding.movieImage);
        binding.movieName.setText(movie.getMovieName());
        binding.movieRate.setText(String.format(Locale.ENGLISH,"%.1f",movie.getImdbRate()));
        String min = getResources().getString(R.string.min);
        binding.movieDuration.setText(String.format(Locale.ENGLISH,"%d %s",movie.getDurationInMinutes(), min));
        binding.movieSummary.setText(movie.getMovieDescription());

        actorsCarouselAdapter = new ActorsCarouselAdapter(this,movie.getCasts());
        categoriesCarouselAdapter = new TextItemCarouselAdapter(this, movie.getCategories());

        binding.actorsCarousel.setAdapter(actorsCarouselAdapter);
        binding.categories.setAdapter(categoriesCarouselAdapter);

        binding.backButton.setOnClickListener(v -> finish());
        binding.likeButton.setOnClickListener(this::likeMovie);
        binding.shareButton.setOnClickListener(this::shareMovie);
    }

    private void likeMovie(View v){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_MOVIES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> favoriteMovieIds = sharedPreferences.getStringSet(SHARED_MOVIES_NAME,new HashSet<>());
        Set<String> newFavoriteMovieIds = new HashSet<>(favoriteMovieIds);
        newFavoriteMovieIds.add(String.valueOf(movie.getMovieId()));
        editor.putStringSet(SHARED_MOVIES_NAME,newFavoriteMovieIds);
        editor.apply();
        Toast.makeText(getApplicationContext(),R.string.movie_liked,Toast.LENGTH_SHORT).show();
    }

    private void shareMovie(View v){
        Intent intent = new Intent(this,ShareActivity.class);

        intent.putExtra(ShareActivity.ARG_MOVIE_NAME,movie.getMovieName());
        intent.putExtra(ShareActivity.ARG_MOVIE_URI,movie.getMovieBannerUri());
        intent.putExtra(ShareActivity.ARG_MOVIE_ID, movie.getMovieId());
        startActivity(intent);
    }
}