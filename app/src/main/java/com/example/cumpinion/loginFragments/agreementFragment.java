package com.example.cumpinion.loginFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cumpinion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class agreementFragment extends Fragment {

    public agreementFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agreement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btNext = view.findViewById(R.id.btNext_agreementFragment);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.fromAgreementToHome);
            }
        });
    }

    /*   Pour réafficher la barre de navigation utiliser ce code: */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Accédez à l'activité parente (MainActivity) pour obtenir la barre de navigation
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        // Rétablir la visibilité de la barre de navigation
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}