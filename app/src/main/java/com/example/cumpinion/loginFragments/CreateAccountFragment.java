package com.example.cumpinion.loginFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cumpinion.R;

import org.json.JSONException;
import org.json.JSONObject;

import classes.RetrofitInstance;
import interfaces.InterfaceServeur;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateAccountFragment extends Fragment {


    public CreateAccountFragment() {
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
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etPrenom = view.findViewById(R.id.etPrenom_CreateAccountFragment);
        EditText etNom = view.findViewById(R.id.etNom_CreateAccountFragment);
        EditText etPseudo = view.findViewById(R.id.etPseudo_CreateAccountFragment);
        EditText etEmail = view.findViewById(R.id.etEmail_CreateAccountFragment);
        EditText etPassword = view.findViewById(R.id.etPassword_CreateAccountFragment);
        EditText etConfirmPassword = view.findViewById(R.id.etConfirmPassword_CreateAccountFragment);

        Button btCreate = view.findViewById(R.id.btCreateAccount_CreateAccountFragment);
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prenom = etPrenom.getText().toString();
                String nom = etNom.getText().toString();
                String pseudo = etPseudo.getText().toString();
                String email  = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if(confirmPassword.equals(password)){

                    // Créer un objet JSON contenant les informations d'identification
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("prenom", prenom);
                        jsonObject.put("nom", nom);
                        jsonObject.put("pseudo", pseudo);
                        jsonObject.put("email", email);
                        jsonObject.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                    Call<Void> call = serveur.register(requestBody);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("Réussi!", "!!!!!!!Compte Crée  : " + email +" "+ password);
                            // Vous pouvez maintenant naviguer vers l'écran suivant ou effectuer d'autres actions
                            NavController controller = Navigation.findNavController(view);
                            controller.navigate(R.id.loginFragment);
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("Réussi!", t.getMessage());

                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "Les mots de passe ne concordent pas", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }
}