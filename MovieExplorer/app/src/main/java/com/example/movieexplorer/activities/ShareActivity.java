package com.example.movieexplorer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.movieexplorer.R;
import com.example.movieexplorer.databinding.ActivityShareBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class ShareActivity extends AppCompatActivity {

    private static final String TAG = "ACTIVITY_SHARE";
    private int movieId;

    public static final String ARG_MOVIE_URI = "movie_uri";
    public static final String ARG_MOVIE_NAME = "movie_name";
    public static final String ARG_MOVIE_ID = "movie_id";

    ActivityShareBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShareBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String movieName = intent.getStringExtra(ARG_MOVIE_NAME);
        String movieUri = intent.getStringExtra(ARG_MOVIE_URI);
        movieId = intent.getIntExtra(ARG_MOVIE_URI,1);

        Glide.with(this).load(movieUri).into(binding.shareContainer.shareMovieImage);
        binding.shareContainer.shareMovieName.setText(movieName);
        binding.buttonShare.setOnClickListener(this::shareMovie);
        binding.buttonCancel.setOnClickListener(v -> finish());
    }


    private Bitmap createBitmapFromView(View view){
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private File parseFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        File cacheFolder = new File(getCacheDir(),"images");
        if (!cacheFolder.exists())
            cacheFolder.mkdirs();
        File cacheDir = new File(cacheFolder, String.format(Locale.ENGLISH,"Share_%d.png",movieId));
        try(FileOutputStream outputStream = new FileOutputStream(cacheDir)){
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();
        } catch (IOException e) {
            Log.e(TAG, "parseFromBitmap: Couldn't create file", e);;
        }
        return cacheDir;
    }
    private Uri getImageUri(Context context, File imageFile) {
        return FileProvider.getUriForFile(
                context,
                context.getApplicationContext().getPackageName() + ".provider",
                imageFile
        );
    }
    private void shareMovie(View view){
        Bitmap bitmap = createBitmapFromView(binding.shareContainer.shareContainer);
        File file = parseFromBitmap(bitmap);
        Uri uri = getImageUri(getApplicationContext(),file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");

        startActivity(Intent.createChooser(shareIntent,getResources().getString(R.string.share)));
    }
}