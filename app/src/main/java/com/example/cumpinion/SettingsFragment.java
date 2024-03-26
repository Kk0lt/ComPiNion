package com.example.cumpinion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cumpinion.loginFragments.LoggedUserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;

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

    private SharedPreferences sharedPreferences;

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

        sharedPreferences = getActivity().getSharedPreferences("LanguagePrefs", getActivity().MODE_PRIVATE);

        String langue = sharedPreferences.getString("language", null);
        if (langue != null) {
            changeLangue(langue);
        } else {
            changeLangue("eng");
        }

        LoggedUserViewModel loggedUserViewModel = new ViewModelProvider(getActivity()).get(LoggedUserViewModel.class);

        //appels des différentes méthodes

        changePassword(tvChangePwd, loggedUserViewModel);
        changerLangue(tvLanguage);
        changeUsername(tvChangePseudo, loggedUserViewModel);
        changerTheme(tvChangeTheme);
        deconnexion(view, tvLogout);

    }
    /*========== Méthodes privées ==========*/

    private void changePassword(TextView tvChangePwd, LoggedUserViewModel loggedUserViewModel) {
        tvChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                View view = getLayoutInflater().inflate(R.layout.dialogbox_password,null);
                builder.setView(view);

                AlertDialog alertDialog = builder.create();

                TextView tvLabel = view.findViewById(R.id.label_passwordDialog);
                tvLabel.setText(R.string.settings_password);

                //DECLARATION
                Button btnConfirm = view.findViewById(R.id.btnConfirmer_dialogPwd);
                Button btnCancel = view.findViewById(R.id.btnCancel_DialogPwd);

                EditText etNewPassword = view.findViewById(R.id.etNewPassword_DialogPwd);
                EditText etCurrentPassword = view.findViewById(R.id.etCurrentPassword_DialogPwd);
                EditText etConfirmPassword = view.findViewById(R.id.etConfirmPassword_DialogPwd);

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    String newPassword = etNewPassword.getText().toString();
                    String confirmPassword = etConfirmPassword.getText().toString();
                    String currentPassword = etCurrentPassword.getText().toString();
                    String loggedPassword =loggedUserViewModel.getUserMutableLiveData().getValue().getPassword();
                    boolean mdpValide = true;


                    if (newPassword.equals(loggedPassword)){
                        etConfirmPassword.setError("le nouveau mot de passe ne doit pas correspondre à l'ancien");
                        etNewPassword.setError("le nouveau mot de passe ne doit pas correspondre à l'ancien");
                        mdpValide = false;
                    }
                    if (newPassword.equals(currentPassword)){
                        etNewPassword.setError("le nouveau mot de passe ne doit pas correspondre à l'ancien");
                        etConfirmPassword.setError("le nouveau mot de passe ne doit pas correspondre à l'ancien");
                        mdpValide = false;
                    }
                    if (!newPassword.equals(confirmPassword)){
                        etConfirmPassword.setError("les mots de passe ne correspondent pas");
                        etNewPassword.setError("les mots de passe ne correspondent pas");
                        mdpValide = false;
                    }

                    if (mdpValide){
                        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
                        Call<Void> call = serveur.updatePassword(loggedUserViewModel.getUserMutableLiveData().getValue().getId(), currentPassword, newPassword);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                loggedUserViewModel.setUserPassword(newPassword);
                                Log.d("failed", "Password update" + newPassword + currentPassword);

                                alertDialog.dismiss();
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.d("failed", "Password update failed: "+ t.getMessage());
                            }
                        });
                    }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });
                // Create the Alert dialog
                // Show the Alert Dialog box
                alertDialog.show();
            }
        });
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
                Button btnConfirm = view.findViewById(R.id.btnConfirmer_dialogBox);
                Button btnCancel = view.findViewById(R.id.btnCancel_DialogBox);
                AlertDialog alertDialog1 = builder.create();

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newPseudo = etNewPseudo.getText().toString();
                        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
                        Call<Void> call = serveur.updatePseudo(loggedUserViewModel.getUserMutableLiveData().getValue().getId(), newPseudo);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Snackbar.make(view, "Done ! ", BaseTransientBottomBar.LENGTH_LONG).show();
                                loggedUserViewModel.setUserPseudo(newPseudo);
                                Log.d("Réussi!", "Pseudo viewmodel : " + loggedUserViewModel.getUserMutableLiveData().getValue().getPseudo());
                                alertDialog1.dismiss();

                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.d("failure!", "failure : " + t.getMessage());
                            }
                        });
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();

                    }
                });
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

    private void changerLangue(TextView tvLanguage) {
        tvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String langue = sharedPreferences.getString("language", null);
                if (langue != null) {
                    if (langue.equalsIgnoreCase("eng")) {
                        changeLangue("fr");
                        getActivity().recreate();
                    }
                    else {
                        changeLangue("eng");
                        getActivity().recreate();

                    }
                } else {
                    changeLangue("fr");
                }
            }
        });

    }

    private void changerTheme(TextView tvTheme) {
        tvTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("@string/settings_themes");
                builder.setMessage("@string/choosetext");
                builder.setCancelable(false);

                // Bleu
                builder.setPositiveButton("@string/blue", (DialogInterface.OnClickListener) (dialog, which) -> {
                    changeTheme(R.style.AppTheme_Blue);
                });
                // Rouge
                builder.setNegativeButton("@string/red", (DialogInterface.OnClickListener) (dialog, which) -> {
                    changeTheme(R.style.AppTheme_Red);
                });
                // Vert
                builder.setNeutralButton("@string/green", (DialogInterface.OnClickListener) (dialog, which) -> {
                    changeTheme(R.style.AppTheme_Green);
                });

                AlertDialog alertTheme = builder.create();
                alertTheme.show();
            }
        });
    }

        /**
         * Réafficher la barre de navigation : */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    /**
     * Changer la langue */
    private void changeLangue(String selectedLanguage) {
        Locale locale = new Locale(selectedLanguage);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", selectedLanguage);
        editor.apply();

    }

    /**
     * Changer le thème */
    private void changeTheme(int themeId) {
        getActivity().setTheme(themeId);
        getActivity().recreate();
    }

}