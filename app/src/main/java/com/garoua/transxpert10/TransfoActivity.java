package com.garoua.transxpert10;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

public class TransfoActivity extends AppCompatActivity {

    TextView designation;
    TextView description;
    TextView region;
    TextView puissance;
    ImageView imageView;
    Button transfoActivity_btn;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfo);

        designation = findViewById(R.id.transfoActivity_designation);
        description = findViewById(R.id.transfoActivity_description);
        region = findViewById(R.id.transfoActivity_region);
        puissance = findViewById(R.id.transfoActivity_puissance);
        imageView = findViewById(R.id.image_transfo);
        transfoActivity_btn = findViewById(R.id.transfoActivity_btn);

        trans_item transItem = getIntent().getParcelableExtra("Transfo");

        designation.setText(transItem.getDesignation());
        description.setText(transItem.getDescription());
        region.setText(transItem.getRegion());
        puissance.setText(Integer.toString(transItem.getPuissance()));
        Glide.with(this)
                .load(transItem.getImage())
                .centerCrop()
                .into(imageView);

        // Ecoute du boutton pour renvoyer vers google maps
        transfoActivity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Création d'un objet Uri à partir des coordonnées géographiques
                String uri = String.format(Locale.ENGLISH, "https://www.google.com/maps?q=%f,%f", transItem.getLongitude(), transItem.getlatitude());

                // Création de l'Intent pour ouvrir Google Maps
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));


                // Lancement de l'Intent
                startActivity(intent);
            }
        });
    }
}