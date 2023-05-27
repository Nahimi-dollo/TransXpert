package com.garoua.transxpert10.ui.dashboard;


import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.garoua.transxpert10.R;
import com.garoua.transxpert10.profil_item;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {
    TextView name_profil;
    TextView fonction_profil;
    ImageView image_profil;
    TextView profil_image;
    int nbre_Nord = 0;
    int nbre_ExtremeNord = 0;
    int nbre_Adamaoua = 0;
    int nbre_Centre = 0;
    int nbre_Sud = 0;
    int nbre_Est = 0;
    int nbre_Ouest = 0;
    int nbre_NordOuest = 0;
    int nbre_SudOuest = 0;
    int nbre_Littoral = 0;
    // Accéder à l'instance de Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Accéder à la collection "transformateur"
    CollectionReference notesRef = db.collection("profil");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    //@SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        String filename = "profil_image.png";
        String path = Environment.getExternalStorageDirectory().toString();
        try {
            File file = new File(path, filename);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            image_profil.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }




        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String value = user.getEmail();

        Query query = notesRef.whereEqualTo("email", value);

        CollectionReference notesRef = db.collection("transformateur");

        // Tableau de toutes les régions que vous souhaitez récupérer le nombre de documents
        String[] regions = new String[]{"Extreme-Nord", "Nord", "Adamaoua", "Centre", "Est", "Ouest", "Nord-Ouest", "Sud-Ouest", "Littoral", "Sud"};

        // Boucle pour parcourir les regions et affecter le nombre au cas ou c'est la region
        for (String region : regions) {
            notesRef.whereEqualTo("region", region).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        int numberOfDocuments = task.getResult().size();
                        switch (region){
                            case "Nord":
                                nbre_Nord = numberOfDocuments;
                                break;
                            case "Extreme-Nord":
                                nbre_ExtremeNord = numberOfDocuments;
                                break;
                            case "Adamaoua":
                                nbre_Adamaoua = numberOfDocuments;
                                break;
                            case "Centre":
                                nbre_Centre = numberOfDocuments;
                                break;
                            case "Est":
                                nbre_Est = numberOfDocuments;
                                break;
                            case "Ouest":
                                nbre_Ouest = numberOfDocuments;
                                break;
                            case "Nord-Ouest":
                                nbre_NordOuest = numberOfDocuments;
                                break;
                            case "Sud-Ouest":
                                nbre_SudOuest = numberOfDocuments;
                                break;
                            case "Littoral":
                                nbre_Littoral = numberOfDocuments;
                                break;
                            case "Sud":
                                nbre_Sud = numberOfDocuments;
                                break;
                        }
                    } else {
                        Log.d(TAG, "Erreur en recuperant les documents pour la région " + region + " : ", task.getException());
                    }
                }
            });
        }




        // Création du graphique des statistiques
        //Toast.makeText(getContext(), NombreTransfo.Nord(), Toast.LENGTH_SHORT).show();

        BarChart barChart = view.findViewById(R.id.chart);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, nbre_ExtremeNord));
        entries.add(new BarEntry(1, nbre_Nord));
        entries.add(new BarEntry(2, nbre_Adamaoua));
        entries.add(new BarEntry(3, nbre_Centre));
        entries.add(new BarEntry(4, nbre_Est));
        entries.add(new BarEntry(5, nbre_Ouest));
        entries.add(new BarEntry(6, nbre_NordOuest));
        entries.add(new BarEntry(7, nbre_SudOuest));
        entries.add(new BarEntry(8, nbre_Littoral));
        entries.add(new BarEntry(9, nbre_Sud));

        BarDataSet bardataset = new BarDataSet(entries, "regions");

        BarData data = new BarData(bardataset);
        barChart.setData(data);

        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);

        bardataset.setColors(Color.GRAY);
        bardataset.setValueTextSize(10f);

        data.setBarWidth(0.9f);

        barChart.animateY(2000);



        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){

                    DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);

                    String nom = doc.getString("nom");
                    String email = doc.getString("email");
                    String password = doc.getString("password");
                    String fonction = doc.getString("fonction");


                    profil_item profilItem = new profil_item(nom, email, fonction, password);
                    name_profil = view.findViewById(R.id.nom_profil);
                    fonction_profil = view.findViewById(R.id.fonction_profil);
                    image_profil = view.findViewById(R.id.imageProfil);

                    name_profil.setText(profilItem.getNom());
                    fonction_profil.setText(profilItem.getFonction());
                }
            }
        });





        //Logique ajout photo de profil
        profil_image = view.findViewById(R.id.btn_profil_image);

        profil_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 100);
            }
        });




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 100) {
            Uri imageUri = data.getData();
            image_profil.setImageURI(imageUri);
            profil_image.setText("");


        }
    }


}