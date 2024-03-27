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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import classes.RetrofitInstance;
import classes.User;
import classes.characters.Compinion;
import classes.characters.CompinionsAdapterList;
import classes.characters.CompinionsReponseServer;
import classes.characters.InterfaceCompinion;
import interfaces.InterfaceServeur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements InterfaceCompinion {

    private SharedPreferences sharedPreferences;
    RecyclerView rvCharacters;
    CompinionsAdapterList compinionsAdapterList;
    LoggedUserViewModel loggedUserViewModel;
    int newCompinionId;
    int originalCompanionID;
    Mqtt5Client client;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost("172.16.87.61")
                .serverPort(1883)
                .simpleAuth()
                .username("cedric")
                .password("q".getBytes())
                .applySimpleAuth()
                .build();
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
        TextView tvChangeCompanion = view.findViewById(R.id.tvChangeCompanion_Settings);
        TextView tvLanguage = view.findViewById(R.id.tvLanguage_Settings);
        TextView tvLogout = view.findViewById(R.id.tvLogout_Settings);

        loggedUserViewModel = new ViewModelProvider(getActivity()).get(LoggedUserViewModel.class);

        originalCompanionID =  loggedUserViewModel.getUserMutableLiveData().getValue().getCompanion_id();


        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);

        sharedPreferences = getActivity().getSharedPreferences("Prefs", getActivity().MODE_PRIVATE);

        String langue = sharedPreferences.getString("language", null);
        if (langue != null) {
            changeLangue(langue);
        } else {
            changeLangue("eng");
        }


        //appels des différentes méthodes
        changeUsername(tvChangePseudo, loggedUserViewModel);
        changePassword(tvChangePwd, loggedUserViewModel);
        changeCompanion(tvChangeCompanion, serveur);

        changerLangue(tvLanguage);
        changerTheme(tvChangeTheme);
        deconnexion(view, tvLogout);

    }


    /*========== MQTT ==========*/

    private void confirmPublishDeconnexion() {
        client.toAsync().connect()
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        Log.d("Fail", "ERREUR MQTT ");
                    } else {
                        // setup subscribes or start publishing
                        publishDeconnexion();
                    }
                });
    }
    private void publishDeconnexion(){
        client.toAsync().publishWith()
                .topic("status")
                .payload("non connecté".getBytes())
                .send()
                .whenComplete((publish, throwable) -> {
                    if (throwable != null) {
                        // handle failure to publish
                    } else {
                        // handle successful publish, e.g. logging or incrementing a metric
                        Log.d("publishConnexion", "connexion published" );

                    }
                });
    }
    private void confirmPublishConnexion(String _username, String _merites, String _img, String _streak) {
        client.toAsync().connect()
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        Log.d("Fail", "ERREUR MQTT ");
                    } else {
                        // setup subscribes or start publishing
                        publishConnexion();
                        publishUser(_username);
                        publishMerites(_merites);
                        publishImage(_img);
                        publishStreak(_streak);
                    }
                });
    }
    private void publishConnexion(){
        client.toAsync().publishWith()
                .topic("status")
                .payload("connecté".getBytes())
                .send()
                .whenComplete((publish, throwable) -> {
                    if (throwable != null) {
                        // handle failure to publish
                    } else {
                        // handle successful publish, e.g. logging or incrementing a metric
                        Log.d("publishConnexion", "connexion published" );

                    }
                });
    }

    /**
     * METHODE DE PUBLISH DU NOM D'UTILISATEUR
     */

    private void publishUser(String username){
        client.toAsync().publishWith()
                .topic("user")
                .payload(username.getBytes())
                .send()
                .whenComplete((publish, throwable) -> {
                    if (throwable != null) {
                        // handle failure to publish
                    } else {
                        // handle successful publish, e.g. logging or incrementing a metric
                        Log.d("publishConnexion", "user published" );

                    }
                });
    }

    /**
     * METHODE DE PUBLISH DE L'IMAGE
     */

    private void publishImage(String img){
        client.toAsync().publishWith()
                .topic("pic")
                .payload(img.getBytes())
                .send()
                .whenComplete((publish, throwable) -> {
                    if (throwable != null) {
                        // handle failure to publish
                    } else {
                        // handle successful publish, e.g. logging or incrementing a metric
                        Log.d("publishConnexion", "connexion published" );

                    }
                });
    }

    /**
     * METHODE DE PUBLISH DES POINTS DE MERITES
     */

    private void publishMerites(String level){
        client.toAsync().publishWith()
                .topic("level")
                .payload(level.getBytes())
                .send()
                .whenComplete((publish, throwable) -> {
                    if (throwable != null) {
                        // handle failure to publish
                    } else {
                        // handle successful publish, e.g. logging or incrementing a metric
                        Log.d("publishConnexion", "connexion published" );

                    }
                });
    }

    /**
     * METHODE DE PUBLISH DES POINTS DE MERITES
     */

    private void publishStreak(String streak){
        client.toAsync().publishWith()
                .topic("streak")
                .payload(streak.getBytes())
                .send()
                .whenComplete((publish, throwable) -> {
                    if (throwable != null) {
                        // handle failure to publish
                    } else {
                        // handle successful publish, e.g. logging or incrementing a metric
                        Log.d("publishConnexion", "connexion published" );
                    }
                });
    }
    /*========== Méthodes privées ==========*/

    /**
     * CHANGER COMPANION
     */

    private void changeCompanion(TextView tvChangeCompanion, InterfaceServeur serveur) {
        tvChangeCompanion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                View view = getLayoutInflater().inflate(R.layout.dialogbox_image,null);
                builder.setView(view);
                AlertDialog alertDialogCompanion = builder.create();
                Button btnConfirm = view.findViewById(R.id.btnConfirmer_dialogImg);
                Button btnCancel = view.findViewById(R.id.btnCancel_dialogImg);

                rvCharacters = view.findViewById(R.id.rvNewCompanion_dialogImg);
                rvCharacters.setHasFixedSize(true);
                rvCharacters.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));

                showSelectableCompanions(serveur);
               // createUserViewModel.getUserMutableLiveData().getValue().setCompanion_id();
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Call<Void> call = serveur.updateCompanion(loggedUserViewModel.getUserMutableLiveData().getValue().getId(), loggedUserViewModel.getUserMutableLiveData().getValue().getCompanion_id());
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Log.d("failed", "Companion ID : " + loggedUserViewModel.getUserMutableLiveData().getValue().getCompanion_id() );
                                    Toast.makeText(getContext(), "le changement apparaitra à la prochaine connexion ", Toast.LENGTH_SHORT).show();
                                    alertDialogCompanion.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.d("failed", "failed changing companion : "+t.getMessage()+ " : " + newCompinionId );
                                    alertDialogCompanion.dismiss();
                                }
                            });

                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialogCompanion.dismiss();
                        }
                    });
                alertDialogCompanion.show();

            }

        });
    }
    /**
     * Methode pour changer de Mot de passe
     * */
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

                TextView tvErr = view.findViewById(R.id.errNewPass);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    String newPassword = etNewPassword.getText().toString();
                    String confirmPassword = etConfirmPassword.getText().toString();
                    String currentPassword = etCurrentPassword.getText().toString();
                    String loggedPassword =loggedUserViewModel.getUserMutableLiveData().getValue().getPassword();
                    boolean mdpValide = true;

                    if(newPassword.trim().isEmpty()){
                        tvErr.setText("Veuillez entrer un nouveau mot de passe");
                        mdpValide = false;
                    }

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
                TextView errModif = view.findViewById(R.id.errNewPass);
                EditText etNewPseudo = view.findViewById(R.id.etNewUsername_DialogBox);
                Button btnConfirm = view.findViewById(R.id.btnConfirmer_dialogBox);
                Button btnCancel = view.findViewById(R.id.btnCancel_DialogBox);
                AlertDialog alertDialog1 = builder.create();

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newPseudo = etNewPseudo.getText().toString().trim();

                        if(newPseudo.trim().isEmpty() || estNumeric(newPseudo) || newPseudo.length() > 14){
                            errModif.setText("Veuillez entrer un pseudo valide");
                        }

                        else {
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
                        String l = sharedPreferences.getString("loggedUser", null);
                        if(l != null) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("loggedUser");
                            editor.apply();
                        }
                        confirmPublishDeconnexion();
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
     * Methode de changement de langue
     * */
    private void changerLangue(TextView tvLanguage) {
        tvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creation d'une boite de dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // Set message
                builder.setMessage("Êtes-vous sûr de vouloir changer la langue ? ?");

                // Set  Title
                builder.setTitle("Changer Langue");
                builder.setCancelable(false);
                builder.setPositiveButton("Oui", (DialogInterface.OnClickListener) (dialog, which) -> {

                    String langue = sharedPreferences.getString("language", null);
                    if (langue != null) {
                        if (langue.equalsIgnoreCase("eng")) {
                            changeLangue("fr");
                            getActivity().recreate();
                        } else {
                            changeLangue("eng");
                            getActivity().recreate();

                        }
                    } else {
                        changeLangue("fr");
                    }
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
     * Methode de changement de Theme
     * */
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
    /**
     * Methode pour afficher les companions de la base de donnés
     * */
    private void showSelectableCompanions(InterfaceServeur serveur) {
        Call<CompinionsReponseServer> call = serveur.getAllCompinions();
        call.enqueue(new Callback<CompinionsReponseServer>() {
            @Override
            public void onResponse(Call<CompinionsReponseServer> call, Response<CompinionsReponseServer> response) {
                Log.d("on response", "response character list");
                CompinionsReponseServer reponseServer = response.body();
                List<Compinion> compinions = reponseServer.getCompinionList();
                compinionsAdapterList = new CompinionsAdapterList(compinions, SettingsFragment.this);
                rvCharacters.setAdapter(compinionsAdapterList);
            }

            @Override
            public void onFailure(Call<CompinionsReponseServer> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur character list");
                Log.d("erreur", t.getMessage());
            }
        });
    }
    @Override
    public void gestionClic(Compinion compinion) {
        newCompinionId = compinion.getId();
        loggedUserViewModel.setUserCompanion(newCompinionId);
        Log.d("gestionClic", "new id: "+ loggedUserViewModel.getUserMutableLiveData().getValue().getCompanion_id());

    }

    private boolean estNumeric(String string){
        try {
            Double.parseDouble(string);
            return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }
}