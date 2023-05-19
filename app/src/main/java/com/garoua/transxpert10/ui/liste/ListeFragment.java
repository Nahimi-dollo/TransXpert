package com.garoua.transxpert10.ui.liste;

import static android.content.ContentValues.TAG;

import static java.lang.Integer.parseInt;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.garoua.transxpert10.R;
import com.garoua.transxpert10.TransfoActivity;
import com.garoua.transxpert10.databinding.FragmentListeBinding;
import com.garoua.transxpert10.trans_item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListeFragment extends Fragment {

    private FragmentListeBinding binding;
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
        Button searchButton = view.findViewById(R.id.search_filter_btn);
        EditText searchEditText = view.findViewById(R.id.search_editText);

        // Accéder à l'instance de Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Accéder à la collection "notes"
        CollectionReference notesRef = db.collection("transformateur");




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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_recup = searchEditText.getText().toString();
                Log.d(TAG, text_recup);
                searchEditText.setText("");


                Query query_2 = notesRef.orderBy("designation");

                query_2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<trans_item> myObjects = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            trans_item myObject = documentSnapshot.toObject(trans_item.class);
                            myObjects.add(myObject);
                        }

                        ArrayList<trans_item> filteredList = new ArrayList<>();
                        for (trans_item myObject : myObjects) {
                            if (myObject.getDesignation().contains(text_recup)) {
                                filteredList.add(myObject);
                            }
                        }

                        listViewAdapter = new ListViewAdapter(getContext(), filteredList);
                        listView.setAdapter(listViewAdapter);
                    }
                });
            }
        });


    }


}