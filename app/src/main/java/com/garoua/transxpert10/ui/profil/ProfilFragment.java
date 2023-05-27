package com.garoua.transxpert10.ui.profil;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.garoua.transxpert10.R;
import com.garoua.transxpert10.ajout_agence;
import com.garoua.transxpert10.connect;
import com.garoua.transxpert10.databinding.FragmentProfilBinding;
import com.garoua.transxpert10.trans_item;
import com.garoua.transxpert10.ui.liste.ListViewAdapter;
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

import java.util.Map;
import java.util.Objects;

public class ProfilFragment extends Fragment {

private FragmentProfilBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        Button dec = view.findViewById(R.id.deconnexion);
        Button ajout = view.findViewById(R.id.activity_chef_d_agence);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        assert user != null;
        String value = user.getEmail();
        CollectionReference prof = db.collection("profil");

        Query query = prof.whereEqualTo("email", value);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);

                    if (Objects.equals(doc.getString("fonction"), "chef d'agence")){
                        ajout.setEnabled(false);
                    }
                }
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Récupère la référence de l'instance de FirebaseAuth
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                //Déconnexion de l'utilisateur
                firebaseAuth.signOut();

                //Redirection vers la page de connexion
                Intent intent = new Intent(getContext(), connect.class);
                startActivity(intent);
            }
        });

        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Redirection vers la page de connexion
                Intent intent = new Intent(getContext(), ajout_agence.class);
                startActivity(intent);
            }
        });

    }


}