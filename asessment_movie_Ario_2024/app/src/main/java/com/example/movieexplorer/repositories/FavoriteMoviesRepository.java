package com.example.movieexplorer.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.movieexplorer.activities.MovieDetailsActivity;
import com.example.movieexplorer.models.Movie;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FavoriteMoviesRepository implements Repository<Movie> {

    private MovieRepository repository;
    private Set<String> movieIds;

    private FavoriteMoviesRepository(Set<String> movieIds, MovieRepository repository){
        this.movieIds = movieIds;
        this.repository = repository;
    }

    public static FavoriteMoviesRepository getFromContext(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                MovieDetailsActivity.SHARED_MOVIES_NAME, Context.MODE_PRIVATE
        );
        Set<String> movieIds = sharedPreferences.getStringSet(MovieDetailsActivity.SHARED_MOVIES_NAME,new HashSet<>());
        MovieRepository repository = new MovieRepository();
        return new FavoriteMoviesRepository(movieIds, repository);
    }

    @Override
    public List<Movie> getAll(int page) {
        return movieIds.stream()
                .map(moviId -> repository.findOneById(Integer.parseInt(moviId)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Movie> findOneById(int id) {
        if (this.movieIds.contains(String.valueOf(id))){
            return repository.findOneById(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Movie> findOne(Predicate<Movie> predicate) {
        return repository.findOne(movie -> predicate.test(movie) && this.movieIds.contains(String.valueOf(movie.getMovieId())));
    }

    @Override
    public List<Movie> getAllMatched(Predicate<Movie> predicate) {
        return repository.getAllMatched(movie -> predicate.test(movie) && this.movieIds.contains(String.valueOf(movie.getMovieId())));
    }
}
