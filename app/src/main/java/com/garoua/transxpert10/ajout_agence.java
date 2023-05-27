package com.garoua.transxpert10;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

public class ajout_agence extends AppCompatActivity {

    EditText nom;
    EditText email;
    EditText password;
    EditText agence_;
    Button validation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_agence);

        nom = findViewById(R.id.nom_chef_agence);
        email = findViewById(R.id.email_chef_agence);
        password = findViewById(R.id.password_chef_agence);
        agence_ = findViewById(R.id.agence_chef_agence);
        validation = findViewById(R.id.button_chef_agence);



        //  Logique l'ajout

        // Logique pour l'ajout d'un transformateur

        validation.setOnClickListener(v -> {

            if (nom.getText().toString().equals("") || email.getText().toString().equals("") || Objects.equals(password, "") || agence_.getText().toString().equals("")) {
                Toast.makeText(this, "Champs invalide", Toast.LENGTH_SHORT).show();
            }else{

                // Demande la confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false)
                        .setTitle("Confirmer")
                        .setMessage("Êtes-vous sûr de vouloir continuer?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // L'utilisateur a cliqué sur Accepter

                                // Logique pour imposer a un chef d'agence de n'ajouter que les transfo dans sa region
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                FirebaseAuth auth = FirebaseAuth.getInstance();

                                // Accéder à la collection "profil"
                                CollectionReference profil = db.collection("profil");


                                agence_item agenceItem = new agence_item(agence_.getText().toString(), nom.getText().toString());
                                profil_item profilItem = new profil_item(nom.getText().toString(), email.getText().toString(), "chef d'agence", password.getText().toString());



                                //Logique d'ajout

                                // Demarrage du dialogue de chargement
                                AlertDialog.Builder builder = new AlertDialog.Builder(ajout_agence.this);
                                LayoutInflater inflater = ajout_agence.this.getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.chargement, null);
                                builder.setView(dialogView);

                                AlertDialog dialog_ = builder.create();
                                dialog_.show();

                                // Debut de la logique d'ajout


                                db.collection("agence").add(agenceItem)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {

                                                db.collection("profil").add(profilItem)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {

                                                                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    nom.setText("");
                                                                                    email.setText("");
                                                                                    password.setText("");
                                                                                    agence_.setText("");
                                                                                    dialog_.dismiss();
                                                                                    Toast.makeText(ajout_agence.this, "Ajout réussi !", Toast.LENGTH_SHORT).show();
                                                                                } else {
                                                                                    Toast.makeText(ajout_agence.this, "Erreur de creation !", Toast.LENGTH_SHORT).show();
                                                                                    // Afficher un message d'erreur à l'utilisateur si la création de compte échoue.
                                                                                }
                                                                            }
                                                                        });

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(ajout_agence.this, "Erreur lors de l'ajout du DocumentSnapshot", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });



                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ajout_agence.this, "Erreur lors de l'ajout du DocumentSnapshot", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }

                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // L'utilisateur a cliqué sur Annuler
                                // Vous pouvez ajouter votre code pour annuler l'action ici
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();


            }
        });
    }
}