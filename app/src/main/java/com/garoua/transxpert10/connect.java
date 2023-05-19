package com.garoua.transxpert10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class connect extends AppCompatActivity {

    EditText email;
    EditText password;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect);
        FirebaseApp.initializeApp(this);

        email = findViewById(R.id.chapmsEmailUser);
        password = findViewById(R.id.champsPasswordUser);
        btn = findViewById(R.id.btn_login);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(connect.this, "Veillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // L'utilisateur est connecté avec succès
                                    //firebaseAuth.setPersistenceEnabled(true);
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    Intent dashbordActivity = new Intent(connect.this, dashboard.class);
                                    startActivity(dashbordActivity);
                                } else {
                                    // Une erreur s'est produite
                                    Exception e = task.getException();
                                    Toast.makeText(connect.this, "Utilisateur non existant", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}