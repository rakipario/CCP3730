package com.example.movieexplorer.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.movieexplorer.R;
import com.example.movieexplorer.activities.MovieDetailsActivity;
import com.example.movieexplorer.adapters.BannerCarouselAdapter;
import com.example.movieexplorer.adapters.MovieCarouselAdapter;
import com.example.movieexplorer.adapters.TextItemCarouselAdapter;
import com.example.movieexplorer.databinding.FragmentExploreBinding;
import com.example.movieexplorer.models.Movie;
import com.example.movieexplorer.repositories.MovieRepository;
import com.example.movieexplorer.repositories.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class ExploreFragment extends Fragment {
    private static final String TAG = "FRAGMENT_EXPLORE";

    private BannerCarouselAdapter bannerCarouselAdapter;
    private MovieCarouselAdapter bestMoviesAdapter;
    private MovieCarouselAdapter upcomingMoviesAdapter;
    private TextItemCarouselAdapter categoriesAdapter;
    private FragmentExploreBinding binding;


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loadMovies(){
        Repository<Movie> repository = new MovieRepository();
        Handler handler = new Handler();

        if (isNetworkAvailable()){
            Thread thread = new Thread(() -> {
                List<Movie> movies = repository.getAll(-1);
                bannerCarouselAdapter = new BannerCarouselAdapter(getContext(),movies);
                List<Movie> bests = movies.stream().sorted(Comparator.comparingDouble(Movie::getPopularity)).collect(Collectors.toList());
                bestMoviesAdapter = new MovieCarouselAdapter(getContext(),bests);
                List<Movie> upcoming = movies.stream().sorted(Comparator.comparing(Movie::getReleaseDate)).collect(Collectors.toList());
                upcomingMoviesAdapter = new MovieCarouselAdapter(getContext(),upcoming);

                bannerCarouselAdapter.setOnClickListener(this::moveToMovieDetail);
                bestMoviesAdapter.setOnClickListener(this::moveToMovieDetail);
                upcomingMoviesAdapter.setOnClickListener(this::moveToMovieDetail);

                handler.post(() -> {
                    binding.banner.setAdapter(bannerCarouselAdapter);
                    binding.bestMovies.setAdapter(bestMoviesAdapter);
                    binding.upcomingMovies.setAdapter(upcomingMoviesAdapter);
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

    public void moveToMovieDetail(View v,Movie movie){
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.ARG_MOVIE_ID, movie.getMovieId());
        getActivity().startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater,container,false);
        loadMovies();
        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}