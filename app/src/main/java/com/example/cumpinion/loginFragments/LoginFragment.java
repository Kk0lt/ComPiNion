package com.example.cumpinion.loginFragments;

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

import com.example.cumpinion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
        Button btConnexion = view.findViewById(R.id.btLogin);
        EditText etEmail = view.findViewById(R.id.etEmail_loginFragment);
        EditText etPassword = view.findViewById(R.id.etPassword_loginFragment);
        TextView tvCreateAccount_loginFragment = view.findViewById(R.id.tvCreateAccount_loginFragment);

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
                String email  = etEmail.getText().toString();
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
                                int userId = userData.getInt("id");
                                int userCompanion = userData.getInt("character_id");
                                int jours = userData.getInt("jours");
                                int merite = userData.getInt("merite");
                                int limite = userData.getInt("limite");
                                LoggedUserViewModel loggedUserViewModel =
                                        new ViewModelProvider(getActivity()).get(LoggedUserViewModel.class);

                                // Créer un nouvel objet User avec les informations récupérées
                                User loggedUser = new User(userId,userName, userPrenom, userEmail, password, userPseudo,
                                        merite, jours, userCompanion, limite);
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