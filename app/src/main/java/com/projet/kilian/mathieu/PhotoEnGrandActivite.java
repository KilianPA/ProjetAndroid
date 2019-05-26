package com.projet.kilian.mathieu;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class PhotoEnGrandActivite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_en_grand_activite);

        ImageView image = findViewById(R.id.image_grande);
        Intent sonIntent = getIntent();
        Bundle sonBundle = sonIntent.getExtras();
        if (sonBundle.getString("path") == null) {
            Log.i("LOG Kilian", "URL");
            Picasso.get().load(sonBundle.getString("url")).into(image);
        } else {
            Log.i("LOG Kilian", "PATH");
            File f = new File(sonBundle.getString("path"));
            Picasso.get().load(f).into(image);
        }

    }
}
