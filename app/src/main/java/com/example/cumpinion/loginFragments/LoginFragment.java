package com.example.cumpinion.loginFragments;

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

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cumpinion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import classes.RetrofitInstance;
import classes.User;
import interfaces.InterfaceServeur;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    private RadioGroup languageRadio;
    private CheckBox checkLog;
    private SharedPreferences sharedPreferences;
    Mqtt5Client client;

    public LoginFragment() {
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
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("Prefs", getActivity().MODE_PRIVATE);

        String l = sharedPreferences.getString("loggedUser", null);
        if(l != null) {
            LoggedUserViewModel loggedUserViewModel =
                    new ViewModelProvider(getActivity()).get(LoggedUserViewModel.class);
            Gson gson = new Gson();
            User user = gson.fromJson(l, User.class);
            loggedUserViewModel.addUser(user);
            confirmPublishConnexion(user.getPseudo(), String.valueOf(user.getMerite()), user.getCompanionPNG(), String.valueOf(user.getJours()));
            NavController controller = Navigation.findNavController(view);
            controller.navigate(R.id.fromLoginToHome);
        }

        String langue = sharedPreferences.getString("language", null);
        if (langue != null) {
            changeLangue(langue);
            RadioButton radioButton = view.findViewWithTag(langue);
            if (radioButton != null) {
                radioButton.setChecked(true);
            }
        }

        Button btConnexion = view.findViewById(R.id.btLogin);
        EditText etEmail = view.findViewById(R.id.etEmail_loginFragment);
        EditText etPassword = view.findViewById(R.id.etPassword_loginFragment);
        TextView tvCreateAccount_loginFragment = view.findViewById(R.id.tvCreateAccount_loginFragment);
        languageRadio = view.findViewById(R.id.toggleLanguage);
        checkLog = view.findViewById(R.id.checkLogged);
        tvCreateAccount_loginFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.fromLoginToCreateAccount);
            }
        });

        // Accédez à l'activité parente (MainActivity) pour obtenir la barre de navigation
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        // Cacher la barre de navigation
        bottomNavigationView.setVisibility(View.GONE);

        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);

        btConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean valide = true;

                if (TextUtils.isEmpty(etEmail.getText().toString())){
                    etEmail.setError("Entrez votre email");
                    valide = false;
                }
                if (TextUtils.isEmpty(etPassword.getText().toString())){
                    etPassword.setError("Entrez votre mot de passe");
                    valide = false;
                }
                else if (valide = true) {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();

                    // Créer un objet JSON contenant les informations d'identification
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email", email);
                        jsonObject.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                    Call<ResponseBody> call = serveur.login(requestBody);

                     call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    // Récupérer le corps de la réponse sous forme de chaîne
                                    String responseBody = response.body().string();
                                    JSONObject jsonObject = new JSONObject(responseBody);
                                    JSONObject userData = jsonObject.getJSONObject("user");
                                    JSONObject userCompanion = jsonObject.getJSONObject("companion");

                                    // Récupérer les informations de l'utilisateur depuis l'objet JSON
                                    String userName = userData.getString("nom");
                                    String userPrenom = userData.getString("prenom");
                                    String userEmail = userData.getString("email");
                                    String userPseudo = userData.getString("pseudo");
                                    String companionImage = userCompanion.getString("img");
                                    int imgId = userData.getInt("character_id");
                                    int userId = userData.getInt("id");
                                    int jours = userData.getInt("jours");
                                    int merite = userData.getInt("merite");
                                    int limite = userData.getInt("limite");
                                    LoggedUserViewModel loggedUserViewModel =
                                            new ViewModelProvider(getActivity()).get(LoggedUserViewModel.class);

                                    // Créer un nouvel objet User avec les informations récupérées
                                    User loggedUser = new User(userId, userName, userPrenom, userEmail, password, userPseudo,
                                            merite, jours, imgId, limite, companionImage);
                                    Log.d("Réussi!", "Connected : " + loggedUser);
                                    loggedUserViewModel.addUser(loggedUser);

                                    confirmPublishConnexion(userPseudo, String.valueOf(merite), companionImage, String.valueOf(jours));

                                    if(checkLog.isChecked()) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        Gson gson = new Gson();
                                        String jsonUser = gson.toJson(loggedUser);
                                        editor.putString("loggedUser", jsonUser);
                                        editor.apply();
                                    } else {
                                        String l = sharedPreferences.getString("loggedUser", null);
                                        if(l != null) {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.remove("loggedUser");
                                            editor.apply();
                                        }
                                    }

                                    // Vous pouvez maintenant naviguer vers l'écran suivant ou effectuer d'autres actions
                                    NavController controller = Navigation.findNavController(view);
                                    controller.navigate(R.id.fromLoginToHome);

                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                // La connexion a échoué
                                Toast.makeText(getContext(), "Vérifiez vos informations de connexion", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("fail", t.getMessage());

                        }
                    });
                }
            }
        });

        languageRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton selectedRadioButton = view.findViewById(checkedId);
                String selectedLanguage = selectedRadioButton.getTag().toString();

                changeLangue(selectedLanguage);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("language", selectedLanguage);
                editor.commit();

                getActivity().recreate();
            }
        });
    }

    private void changeLangue(String selectedLanguage) {
        Locale locale = new Locale(selectedLanguage);
        Locale.setDefault(locale);
        Resources resources = getActivity().getApplicationContext().getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        //resources.updateConfiguration(config, resources.getDisplayMetrics());
        getActivity().getApplicationContext().createConfigurationContext(config);
    }

    /*===== MQTT ===== */
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