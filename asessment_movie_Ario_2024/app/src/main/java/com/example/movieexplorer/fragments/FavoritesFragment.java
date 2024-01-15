package com.example.movieexplorer.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movieexplorer.R;
import com.example.movieexplorer.databinding.FragmentFavoritesBinding;
import com.example.movieexplorer.models.Movie;
import com.example.movieexplorer.repositories.FavoriteMoviesRepository;
import com.example.movieexplorer.repositories.Repository;
import java.util.List;


public class FavoritesFragment extends Fragment {

    FragmentFavoritesBinding binding;
    List<Movie> favoriteMovies;

    private void loadMovies(){
        Repository<Movie> favoriteMovieRepository = FavoriteMoviesRepository.getFromContext(requireContext());
        Handler handler = new Handler();
        if (isNetworkAvailable()){
            Thread thread = new Thread(() -> {
                favoriteMovies = favoriteMovieRepository.getAll(-1);
                handler.post(() -> {
                    favoriteMovies
                            .forEach(movie -> {
                                View gridView = LayoutInflater.from(requireContext())
                                        .inflate(R.layout.item_movie_carousel, binding.gridLayout, false);
                                ImageView movieImageView = gridView.findViewById(R.id.movie_item_image);
                                TextView movieNameTv = gridView.findViewById(R.id.movie_item_title);

                                Glide.with(requireContext()).load(movie.getMovieBannerUri()).into(movieImageView);
                                movieNameTv.setText(movie.getMovieName());

                                binding.gridLayout.addView(gridView);

                            });
                });
            });
            thread.start();
        }
        else {
            Toast.makeText(requireContext(), R.string.no_internet,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater,container,false);
        loadMovies();
        return binding.getRoot();
    }
}