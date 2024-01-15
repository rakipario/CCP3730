package com.example.movieexplorer.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;


public class Cast implements Parcelable, Serializable {
    private String name;
    private String surname;
    private String photoUri;

    public Cast(String name, String surname, String photoUri) {
        this.name = name;
        this.surname = surname;
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.surname);
        dest.writeString(this.photoUri);
    }

    public static final Parcelable.Creator<Cast> CREATOR = new Parcelable.Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel source) {
            String name = source.readString();
            String surname = source.readString();
            String photoUri = source.readString();
            return new Cast(name,surname,photoUri);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };
}
