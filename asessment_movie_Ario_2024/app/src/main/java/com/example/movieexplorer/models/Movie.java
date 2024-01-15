package com.example.movieexplorer.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Movie implements Parcelable, Serializable {
    private int movieId;
    private String movieName;
    private String movieDescription;
    private String movieBannerUri;
    private List<String> categories;
    private double imdbRate;
    private int durationInMinutes;
    private double popularity;
    private Date releaseDate;
    private List<Cast> casts;


    public Movie(int movieId, String movieName, String movieDescription, String movieBannerUri, double popularity) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieDescription = movieDescription;
        this.movieBannerUri = movieBannerUri;
        this.popularity = popularity;
    }

    public Movie(int movieId, String movieName, String movieDescription, String movieBannerUri, double popularity, Date releaseDate) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieDescription = movieDescription;
        this.movieBannerUri = movieBannerUri;
        this.popularity = popularity;
        this.releaseDate = releaseDate;
    }

    public Movie(int movieId, String movieName, String movieDescription, String movieBannerUri, double imdbRate, int durationInMinutes) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieDescription = movieDescription;
        this.movieBannerUri = movieBannerUri;
        this.imdbRate = imdbRate;
        this.durationInMinutes = durationInMinutes;
    }

    public Movie(int movieId, String movieName, String movieDescription, String movieBannerUri, List<String> categories, double imdbRate, int durationInMinutes, double popularity, Date releaseDate, List<Cast> casts) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieDescription = movieDescription;
        this.movieBannerUri = movieBannerUri;
        this.categories = categories;
        this.imdbRate = imdbRate;
        this.durationInMinutes = durationInMinutes;
        this.popularity = popularity;
        this.releaseDate = releaseDate;
        this.casts = casts;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getMovieBannerUri() {
        return movieBannerUri;
    }

    public void setMovieBannerUri(String movieBannerUri) {
        this.movieBannerUri = movieBannerUri;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> category) {
        this.categories = category;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
    }

    public double getImdbRate() {
        return imdbRate;
    }

    public void setImdbRate(double imdbRate) {
        this.imdbRate = imdbRate;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.movieId);
        dest.writeString(this.movieName);
        dest.writeString(this.movieDescription);
        dest.writeString(this.movieBannerUri);
        dest.writeStringList(this.categories);
        dest.writeTypedList(this.casts);
        dest.writeDouble(this.imdbRate);
        dest.writeInt(this.durationInMinutes);
        dest.writeDouble(this.popularity);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        dest.writeString(simpleDateFormat.format(this.releaseDate));
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            int movieId = source.readInt();
            String movieName = source.readString();
            String movieDescription = source.readString();
            String movieBannerUri = source.readString();
            List<String> categories = new ArrayList<>();
            source.readStringList(categories);
            List<Cast> casts = new ArrayList<>();
            source.readTypedList(casts, Cast.CREATOR);
            int imdbRate = source.readInt();
            int durationInMinutes = source.readInt();
            double popularity = source.readDouble();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
            Date date = null;
            try {
                date = simpleDateFormat.parse(Objects.requireNonNull(source.readString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return new Movie(
                    movieId,movieName,movieDescription,movieBannerUri,categories,imdbRate,
                    durationInMinutes,popularity,date,casts
            );
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
