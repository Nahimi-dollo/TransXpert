package com.garoua.transxpert10;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        btn = findViewById(R.id.button_connect);

        if (user != null) {
            // l'utilisateur est déjà connecté
            // rediriger vers la page d'accueil
            Intent dash_page = new Intent(MainActivity.this, dashboard.class);
            startActivity(dash_page);
            finish();
        } else {
            // l'utilisateur n'est pas connecté
            // afficher l'écran de connexion
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent connect_page = new Intent(MainActivity.this, connect.class);
                    startActivity(connect_page);

                }
            });
        }
    }
}