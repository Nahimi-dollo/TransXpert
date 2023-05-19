package com.garoua.transxpert10.ui.profil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.garoua.transxpert10.databinding.FragmentProfilBinding;

public class ProfilFragment extends Fragment {

private FragmentProfilBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        ProfilViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ProfilViewModel.class);

    binding = FragmentProfilBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textProfil;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}