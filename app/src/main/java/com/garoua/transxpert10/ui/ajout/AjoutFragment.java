package com.garoua.transxpert10.ui.ajout;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.garoua.transxpert10.R;
import com.garoua.transxpert10.databinding.FragmentAjoutBinding;
import com.garoua.transxpert10.trans_item;
import com.garoua.transxpert10.ui.dashboard.DashboardViewModel;
import com.garoua.transxpert10.ui.liste.ListViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class AjoutFragment extends Fragment{

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;

    EditText designation;
    EditText description;
    EditText latitude;
    EditText longitude;
    EditText ligne;
    EditText imageFound;
    Spinner Spinner_region;
    EditText puissance;
    private Button validation;
    String region;
    double latitude_value;
    double longitude_value;
    Button btn_latitude;
    Button btn_get_image;
    Uri uri;
    Uri downloadUri;
    String imageUrl = "vide";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_ajout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        designation = view.findViewById(R.id.designation);
        description = view.findViewById(R.id.description);
        longitude = view.findViewById(R.id.longtitude);
        ligne = view.findViewById(R.id.ligne);
        imageFound = view.findViewById(R.id.image_found);
        Spinner_region = view.findViewById(R.id.region_spinner);
        latitude = view.findViewById(R.id.latitude);
        puissance = view.findViewById(R.id.puissance);
        validation = view.findViewById(R.id.validation_ajout);
        btn_latitude = view.findViewById(R.id.btn_get_latitude);
        btn_get_image = view.findViewById(R.id.btn_get_image);



        //  Logique l'ajout


            // logique recuperation image

        btn_get_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
            }
        });




            //logique de recuperation de la position
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        btn_latitude.setOnClickListener(v -> {
            // Appeler votre méthode pour récupérer la position actuelle de l'appareil

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // obtenir la position actuelle de l'appareil
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude_value = location.getLatitude();
                    longitude_value = location.getLongitude();
                    longitude.setText(String.valueOf(longitude_value));
                    latitude.setText(String.valueOf(latitude_value));

                    // faire quelque chose avec les coordonnées de latitude et longitude ici
                } else {
                    // Aucune position n'a été récupérée. Afficher un message d'erreur ou demander une nouvelle position.
                }
            } else {
                // Demander la permission de localisation à l'utilisateur
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        });


        // Logique du spinner

        // création d'un ArrayAdapter pour le sipnner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.region_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_region.setAdapter(adapter);

        // Ecoute du spinner
        Spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // récupère la valeur sélectionnée
                region = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // code à exécuter lorsqu'aucun élément n'est sélectionné
            }
        });




          // Logique pour l'ajout d'un transformateur

        validation = (Button) view.findViewById(R.id.validation_ajout);
        validation.setOnClickListener(v -> {

            if (designation.getText().toString().equals("") || description.getText().toString().equals("") || Objects.equals(region, "") || latitude.getText().toString().equals("") || longitude.getText().toString().equals("") || Objects.equals(imageUrl, "") || ligne.getText().toString().equals("") || puissance.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Champs invalide", Toast.LENGTH_SHORT).show();
            }else{


            // Demande la confirmation
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false)
                    .setTitle("Confirmer")
                    .setMessage("Êtes-vous sûr de vouloir continuer?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // L'utilisateur a cliqué sur Accepter

                            // Logique pour imposer a un chef d'agence de n'ajouter que les transfo dans sa region
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser user = auth.getCurrentUser();

                            assert user != null;
                            String value = user.getEmail();
                            CollectionReference prof = db.collection("profil");
                            // Accéder à la collection "transformateur"
                            CollectionReference notesRef = db.collection("transformateur");
                            CollectionReference agence = db.collection("agence");

                            Query query = prof.whereEqualTo("email", value);


                            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if(!queryDocumentSnapshots.isEmpty()){

                                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);

                                        //String nom = doc.getString("nom");
                                        //String email = doc.getString("email");
                                        String region_ = doc.getString("region");
                                        String fonction = doc.getString("fonction");

                                        Query query_agence = agence.whereEqualTo("nom_chef", doc.getString("nom"));
                                        query_agence.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if(!queryDocumentSnapshots.isEmpty()){

                                                    DocumentSnapshot docc = queryDocumentSnapshots.getDocuments().get(0);

                                                    // recuperation des elements de la region specifie
                                                    if (Objects.equals(fonction, "directeur generale")){

                                                    }else if (Objects.equals(fonction, "chef d'agence")){
                                                        region = docc.getString("nom");
                                                        Toast.makeText(getContext(), "La region a ete modifie en votre region", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(getContext(), "Une erreur est survenu", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            }
                                        });


                                    }
                                }
                            });








                            //Logique d'ajout

                            // Demarrage du dialogue de chargement
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = requireActivity().getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.chargement, null);
                            builder.setView(dialogView);

                            AlertDialog dialog_ = builder.create();
                            dialog_.show();

                            // Debut de la logique d'ajout
                            //FirebaseFirestore db = FirebaseFirestore.getInstance();
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference().child("transfoImages");

                            StorageReference imageRef = storageRef.child(System.currentTimeMillis() + "_" + DateFormat.getDateTimeInstance().format(new Date()) + ".jpg");
                            UploadTask uploadTask = imageRef.putFile(uri);


                            uploadTask.addOnSuccessListener(taskSnapshot -> {
                                imageRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                                    imageUrl = downloadUrl.toString();


                                    trans_item transItem = new trans_item(designation.getText().toString(), description.getText().toString(), region, latitude_value, longitude_value, imageUrl, ligne.getText().toString(), parseInt(puissance.getText().toString()));

                                    db.collection("transformateur").add(transItem)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    designation.setText("");
                                                    description.setText("");
                                                    latitude.setText("");
                                                    longitude.setText("");
                                                    puissance.setText("");
                                                    ligne.setText("");
                                                    imageFound.setText("");
                                                    dialog_.dismiss();


                                                    Toast.makeText(getContext(), "Ajout réussi !", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Erreur lors de l'ajout du DocumentSnapshot", e);
                                                }
                                            });

                                });


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                imageFound.setText(uri.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}