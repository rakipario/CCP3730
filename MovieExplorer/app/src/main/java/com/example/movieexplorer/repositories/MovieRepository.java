package com.example.movieexplorer.repositories;

import com.example.movieexplorer.asynctasks.MovieFetcher;
import com.example.movieexplorer.models.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MovieRepository implements Repository<Movie>{
    private MovieFetcher fetcher;

    public MovieRepository() {
        this.fetcher = new MovieFetcher();
    }

    @Override
    public List<Movie> getAll(int page) {
        Optional<List<Movie>> movies = fetcher.getMovies(page);
        return movies.orElse(new ArrayList<>());
    }

    @Override
    public Optional<Movie> findOneById(int id) {
        return fetcher.getMovieById(id);
    }

    @Override
    public Optional<Movie> findOne(Predicate<Movie> predicate) {
        return fetcher.getMovies(-1).orElse(new ArrayList<>()).stream().filter(predicate).findFirst();
    }

    @Override
    public List<Movie> getAllMatched(Predicate<Movie> predicate) {
        return fetcher.getMovies(-1).orElse(new ArrayList<>()).stream().filter(predicate).collect(Collectors.toList());
    }
}
