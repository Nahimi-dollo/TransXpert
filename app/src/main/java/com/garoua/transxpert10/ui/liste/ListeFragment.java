package com.garoua.transxpert10.ui.liste;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.garoua.transxpert10.R;
import com.garoua.transxpert10.TransfoActivity;
import com.garoua.transxpert10.trans_item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ListeFragment extends Fragment {

    ListView listView;
    public ArrayList<trans_item> trans_itemArrayList;
    public ListViewAdapter listViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_liste, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // Demarrage du dialogue de chargement
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.chargement, null);
        builder.setView(dialogView);

        AlertDialog dialog_ = builder.create();
        dialog_.show();








        Button searchButton = view.findViewById(R.id.search_filter_btn);
        EditText searchEditText = view.findViewById(R.id.search_editText);

        // Accéder à l'instance de Firestore
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
                    dialog_.dismiss();

                    DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);

                    //String nom = doc.getString("nom");
                    //String email = doc.getString("email");
                    String region = doc.getString("region");
                    String fonction = doc.getString("fonction");

                    if (Objects.equals(fonction, "directeur generale")){
                        Toast.makeText(getContext(), "directeur generale", Toast.LENGTH_SHORT).show();
                        notesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            // declaration des variables pour stocker les differents element recupere sur firebase

                            String designation;
                            String description;
                            String region;
                            double latitude;
                            double longitude;
                            String image;
                            String ligne;
                            int puissance;

                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                // on verifie si tout est correct
                                if (task.isSuccessful()) {
                                    // on parcours le resultat et on returne une map
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Map<String, Object> list = document.getData();
                                        // on parcours pour chaque objet les different valeurs et cles et attribuons la valeurs a la variable correspondante
                                        for (Map.Entry<String, Object> entry : list.entrySet()) {
                                            String key = entry.getKey();
                                            String value = String.valueOf(entry.getValue());
                                            switch (key){
                                                case "designation":
                                                    designation = value;
                                                    break;
                                                case "description":
                                                    description = value;
                                                    break;
                                                case "region":
                                                    region = value;
                                                    break;
                                                case "latitude":
                                                    latitude = Double.parseDouble(value);
                                                    break;
                                                case "longitude":
                                                    longitude = Double.parseDouble(value);
                                                    break;
                                                case "image":
                                                    image = value;
                                                    break;
                                                case "ligne":
                                                    ligne = value;
                                                    break;
                                                case "puissance":
                                                    puissance = parseInt(value);
                                                    break;
                                            }

                                        }

                                        trans_item trans1 = new trans_item(designation, description, region, latitude, longitude, image, ligne, puissance);
                                        trans_itemArrayList.add(trans1);
                                        listViewAdapter = new ListViewAdapter(getContext(), trans_itemArrayList);
                                        listView.setAdapter(listViewAdapter);
                                        Log.d(TAG, document.getId() + " => " + trans1);

                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }

                        });
                    }else if (Objects.equals(fonction, "chef d'agence")){
                        Toast.makeText(getContext(), "Chef d'agence", Toast.LENGTH_SHORT).show();
                        Query query_agence = agence.whereEqualTo("nom_chef", doc.getString("nom"));
                        query_agence.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(!queryDocumentSnapshots.isEmpty()){

                                    DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);

                                    // recuperation des elements de la region specifie

                                    notesRef.whereEqualTo("region", doc.getString("nom")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                        // declaration des variables pour stocker les differents element recupere sur firebase

                                        String designation;
                                        String description;
                                        String region;
                                        double latitude;
                                        double longitude;
                                        String image;
                                        String ligne;
                                        int puissance;

                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            // on verifie si tout est correct
                                            if (task.isSuccessful()) {
                                                // on parcours le resultat et on returne une map
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Map<String, Object> list = document.getData();
                                                    // on parcours pour chaque objet les different valeurs et cles et attribuons la valeurs a la variable correspondante
                                                    for (Map.Entry<String, Object> entry : list.entrySet()) {
                                                        String key = entry.getKey();
                                                        String value = String.valueOf(entry.getValue());
                                                        switch (key){
                                                            case "designation":
                                                                designation = value;
                                                                break;
                                                            case "description":
                                                                description = value;
                                                                break;
                                                            case "region":
                                                                region = value;
                                                                break;
                                                            case "latitude":
                                                                latitude = Double.parseDouble(value);
                                                                break;
                                                            case "longitude":
                                                                longitude = Double.parseDouble(value);
                                                                break;
                                                            case "image":
                                                                image = value;
                                                                break;
                                                            case "ligne":
                                                                ligne = value;
                                                                break;
                                                            case "puissance":
                                                                puissance = parseInt(value);
                                                                break;
                                                        }

                                                    }

                                                    trans_item trans1 = new trans_item(designation, description, region, latitude, longitude, image, ligne, puissance);
                                                    trans_itemArrayList.add(trans1);
                                                    listViewAdapter = new ListViewAdapter(getContext(), trans_itemArrayList);
                                                    listView.setAdapter(listViewAdapter);
                                                    Log.d(TAG, document.getId() + " => " + trans1);

                                                }
                                            } else {
                                                Log.w(TAG, "Error getting documents.", task.getException());
                                            }
                                        }

                                    });
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getContext(), "Une erreur est survenu", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

        trans_itemArrayList = new ArrayList<>();
        listView = view.findViewById(R.id.listTrans);

        listView.setOnItemClickListener (new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                trans_item transItem = (trans_item) parent.getItemAtPosition(position);

                Intent startActivityTrans = new Intent(getContext(), TransfoActivity.class);
                startActivityTrans.putExtra("Transfo", transItem);

                startActivity(startActivityTrans);
            }
        });


    }


}