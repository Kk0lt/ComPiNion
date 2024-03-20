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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cumpinion.loginFragments.LoggedUserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

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

        LoggedUserViewModel loggedUserViewModel = new ViewModelProvider(getActivity()).get(LoggedUserViewModel.class);

        //appels des différentes méthodes
        changeUsername(tvChangePseudo, loggedUserViewModel);
        deconnexion(view, tvLogout);

    }

    /**
     * Methode pour changer le psuedonyme dans une boite de dialogue
     */
    private void changeUsername(TextView tvChangePseudo, LoggedUserViewModel loggedUserViewModel) {
        tvChangePseudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                View view = getLayoutInflater().inflate(R.layout.dialogbox_pseudo,null);
                builder.setView(view);
                EditText etNewPseudo = view.findViewById(R.id.etNewUsername_DialogBox);

                builder.setPositiveButton("Oui", (DialogInterface.OnClickListener) (dialog, which) -> {
                    String newPseudo = etNewPseudo.getText().toString();
                    InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
                    Call<Void> call = serveur.updatePseudo(loggedUserViewModel.getUserMutableLiveData().getValue().getId(), newPseudo);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Snackbar.make(view, "Done ! ", BaseTransientBottomBar.LENGTH_LONG).show();

                            loggedUserViewModel.setUserPseudo(newPseudo);

                            Log.d("Réussi!", "Pseudo viewmodel : " + loggedUserViewModel.getUserMutableLiveData().getValue().getPseudo());

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("failure!", "failure : " + t.getMessage());

                        }
                    });

                });

                builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                AlertDialog alertDialog1 = builder.create();
                alertDialog1.show();


            }
        });
    }


    /**
    * Methode de deconnexion de l'application
    * */
    private void deconnexion(@NonNull View view, TextView tvLogout) {
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        Call<Void> call = serveur.logout();

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Creation d'une boite de dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // Set message
                builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter? ?");

                // Set  Title
                builder.setTitle("Deconnexion");
                builder.setCancelable(false);

                //buton de confirmation de deconnexion
                builder.setPositiveButton("Oui", (DialogInterface.OnClickListener) (dialog, which) -> {
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        LoggedUserViewModel loggedUserViewModel = new ViewModelProvider(getActivity()).get(LoggedUserViewModel.class);
                        loggedUserViewModel.addUser(new User());
                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.loginFragment);

                        // Accédez à l'activité parente (MainActivity) pour obtenir la barre de navigation
                        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                        // Rétablir la visibilité de la barre de navigation
                        bottomNavigationView.setVisibility(View.GONE);
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "erreur serveur", Toast.LENGTH_SHORT).show();

                    }
                });
                });
                // Negative button.
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

    /**
     * Réafficher la barre de navigation : */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}