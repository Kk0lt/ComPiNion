package com.example.cumpinion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cumpinion.loginFragments.LoggedUserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import classes.ReponseServer;
import classes.RetrofitInstance;
import classes.User;
import classes.characters.Compinion;
import classes.characters.CompinionsReponseServer;
import interfaces.InterfaceServeur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {


    public SettingsFragment() {
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvChangePseudo = view.findViewById(R.id.tvChangePseudo_Settings);
        TextView tvChangePwd = view.findViewById(R.id.tvChangePassword_Settings);
        TextView tvChangeApparence = view.findViewById(R.id.tvChangeApparence_Settings);
        TextView tvChangeTheme = view.findViewById(R.id.tvChangeTheme_Settings);
        TextView tvLanguage = view.findViewById(R.id.tvLanguage_Settings);
        TextView tvLogout = view.findViewById(R.id.tvLogout_Settings);

        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        Call<Void> call = serveur.logout();

        tvChangePseudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        deconnexion(view, tvLogout, call);

    }

    private void deconnexion(@NonNull View view, TextView tvLogout, Call<Void> call) {

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the object of AlertDialog Builder class
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // Set the message show for the Alert time
                builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter? ?");

                // Set Alert Title
                builder.setTitle("Deconnexion");
                builder.setCancelable(false);

                builder.setPositiveButton("Oui", (DialogInterface.OnClickListener) (dialog, which) -> {
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        LoggedUserViewModel loggedUserViewModel = new ViewModelProvider(getActivity()).get(LoggedUserViewModel.class);
                        loggedUserViewModel.addUser(new User());
                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.loginFragment);
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "erreur serveur", Toast.LENGTH_SHORT).show();

                    }
                });
                });
                // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                builder.setNegativeButton("Non", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });
                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();
                // Show the Alert Dialog box
                alertDialog.show();
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
        bottomNavigationView.setVisibility(View.GONE);
    }
}