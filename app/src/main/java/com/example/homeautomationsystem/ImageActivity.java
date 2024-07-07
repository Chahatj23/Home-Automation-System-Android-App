package com.example.homeautomationsystem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = findViewById(R.id.imageView);

        // Retrieve the image URL from the intent extras
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Load the image using AsyncTask
        new LoadImageTask().execute(imageUrl);
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                // Set the bitmap to the ImageView
                imageView.setImageBitmap(result);
            }
        }
    }
}
