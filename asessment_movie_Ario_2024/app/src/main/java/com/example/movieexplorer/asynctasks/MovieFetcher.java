package com.example.movieexplorer.asynctasks;

import android.util.Log;

import com.example.movieexplorer.models.Cast;
import com.example.movieexplorer.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovieFetcher {
    private interface MovieParser{
        public Movie parse(JSONObject jsonObject) throws Exception;
    }
    private static final String MOVIES_URL = "https://app-vpigadas.herokuapp.com/api/movies/";
    private static final String CONTENT_URI = "https://image.tmdb.org/t/p/original//";

    private static final String TAG = "MOVIE_FETCHER";
    private synchronized String readStream(InputStream stream) throws IOException {
        LinkedList<Integer> readedBytes = new LinkedList<>();
        Iterator<Integer> bytesIterator;
        int readedByte;
        while ((readedByte = stream.read()) > -1){
            readedBytes.add(readedByte);
        }
        byte[] bytes = new byte[readedBytes.size()];
        bytesIterator = readedBytes.iterator();
        for (int i = 0; bytesIterator.hasNext();i++){
            bytes[i] = (byte)((int) bytesIterator.next());
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
    private Movie simpleMovieParser(JSONObject movieObject) throws JSONException, ParseException {
        int movieId = movieObject.getInt("id");
        String movieTitle = movieObject.getString("original_title");
        String movieDescription = movieObject.getString("overview");
        String movieBannerUri = movieObject.getString("poster_path");
        double moviePopularity = movieObject.getDouble("popularity");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        Date date = simpleDateFormat.parse(movieObject.getString("release_date"));
        return new Movie(movieId,movieTitle,movieDescription,movieBannerUri,moviePopularity,date);
    }

    private Movie detailedMovieParser(JSONObject movieObject) throws JSONException, ParseException{
        int movieId = movieObject.getInt("id");
        String movieTitle = movieObject.getString("original_title");
        String movieDescription = movieObject.getString("overview");
        String movieBannerUri = movieObject.getString("poster_path");
        double moviePopularity = movieObject.getDouble("popularity");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        Date date = simpleDateFormat.parse(movieObject.getString("release_date"));
        int durationInMinutes = movieObject.getInt("runtime");
        double rateImdb = movieObject.getDouble("vote_average");

        List<String> categories = new ArrayList<>();
        JSONArray categoriesJson = movieObject.getJSONArray("genres");
        for (int i = 0; i < categoriesJson.length(); i++){
            JSONObject categoryJson = categoriesJson.getJSONObject(i);
            categories.add(categoryJson.getString("name"));
        }
        List<Cast> casts = new ArrayList<>();
        JSONArray castsJson = movieObject.getJSONArray("cast");
        for (int i = 0; i < castsJson.length(); i++){
            JSONObject castJson = castsJson.getJSONObject(i);
            String nameAndSurname = castJson.getString("name");
            String photoUri = "";
            if (castJson.has("profile_path"))
                photoUri = String.format(Locale.ENGLISH, "%s%s",CONTENT_URI,castJson.getString("profile_path"));
            casts.add(new Cast(nameAndSurname,"",photoUri));
        }
        return new Movie(
                movieId, movieTitle, movieDescription, movieBannerUri, categories,
                rateImdb, durationInMinutes, moviePopularity, date,casts
        );
    }

    private synchronized Optional<Movie> fetchSingleUrl(URL url){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("content-type","application/json");
            InputStream stream = connection.getInputStream();
            String response = readStream(stream);
            JSONObject jsonObject = new JSONObject(response);
            Movie movie = detailedMovieParser(jsonObject);
            return Optional.of(movie);
        } catch (JSONException | IOException | ParseException  e) {
            Log.e(TAG, "fetchUrl: Couldn't fetch datas", e);;
        }
        return Optional.empty();
    }

    private synchronized Optional<List<Movie>> fetchQueryUrl(URL url){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("content-type","application/json");
            InputStream stream = connection.getInputStream();
            String response = readStream(stream);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            List<Movie> movies = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject movieObject = jsonArray.getJSONObject(i);
                Movie movie = simpleMovieParser(movieObject);
                movies.add(movie);
            }
            return Optional.of(movies);
        } catch (JSONException | IOException | ParseException e) {
            Log.e(TAG, "fetchUrl: Couldn't fetch datas", e);;
        }
        return Optional.empty();
    }

    public Optional<Movie> getMovieById(int id){
        try {
            URL url = new URL(String.format(Locale.ENGLISH,"%s/%d",MOVIES_URL, id));
            Callable<Optional<Movie>> moviesCallable = () -> fetchSingleUrl(url);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<Optional<Movie>> futreMovie = executorService.submit(moviesCallable);
            return futreMovie.get();
        } catch (MalformedURLException | ExecutionException | InterruptedException e) {
            Log.e(TAG, "getMovies: Malformed URL", e);
            return Optional.empty();
        }
    }

    public Optional<List<Movie>> getMovies(int page){
        try {
            URL url = new URL(MOVIES_URL);
            Callable<Optional<List<Movie>>> moviesCallable = () -> fetchQueryUrl(url);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<Optional<List<Movie>>> futreMovie = executorService.submit(moviesCallable);
            return futreMovie.get();
        } catch (MalformedURLException | ExecutionException | InterruptedException e) {
            Log.e(TAG, "getMovies: Malformed URL", e);
            return Optional.empty();
        }
    }
}
