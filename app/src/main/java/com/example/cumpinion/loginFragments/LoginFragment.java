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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cumpinion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

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
    private SharedPreferences sharedPreferences;

    public LoginFragment() {
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
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("LanguagePrefs", getActivity().MODE_PRIVATE);

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

                                    // Récupérer les informations de l'utilisateur depuis l'objet JSON
                                    String userName = userData.getString("nom");
                                    String userPrenom = userData.getString("prenom");
                                    String userEmail = userData.getString("email");
                                    String userPseudo = userData.getString("pseudo");
                                    String imgUrl = jsonObject.getString("nom");
                                    int userId = userData.getInt("id");
                                    int jours = userData.getInt("jours");
                                    int merite = userData.getInt("merite");
                                    int limite = userData.getInt("limite");
                                    LoggedUserViewModel loggedUserViewModel =
                                            new ViewModelProvider(getActivity()).get(LoggedUserViewModel.class);

                                    // Créer un nouvel objet User avec les informations récupérées
                                    User loggedUser = new User(userId, userName, userPrenom, userEmail, password, userPseudo,
                                            merite, jours, imgUrl, limite);
                                    Log.d("Réussi!", "Connected : " + loggedUser);
                                    loggedUserViewModel.addUser(loggedUser);

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
                editor.apply();

                getActivity().recreate();
            }
        });
    }

    private void changeLangue(String selectedLanguage) {
        Locale locale = new Locale(selectedLanguage);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
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