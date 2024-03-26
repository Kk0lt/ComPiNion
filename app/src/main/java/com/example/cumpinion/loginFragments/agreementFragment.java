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
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.cumpinion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import classes.RetrofitInstance;
import interfaces.InterfaceServeur;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
        CheckBox chkAgree = view.findViewById(R.id.chkAgree);
        TextView tvError = view.findViewById(R.id.tvErrAgree);
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        CreateUserViewModel createUserViewModel = new ViewModelProvider(getActivity()).get(CreateUserViewModel.class);

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prenom = createUserViewModel.getUserMutableLiveData().getValue().getPrenom();
                String nom = createUserViewModel.getUserMutableLiveData().getValue().getNom();
                String pseudo = createUserViewModel.getUserMutableLiveData().getValue().getPseudo();
                String email = createUserViewModel.getUserMutableLiveData().getValue().getEmail();
                String password = createUserViewModel.getUserMutableLiveData().getValue().getPassword();
                int limite = createUserViewModel.getUserMutableLiveData().getValue().getLimite();
                int id = createUserViewModel.getUserMutableLiveData().getValue().getCompanion_id();



                //Vérification du cochage
                if(chkAgree.isChecked()) {
    
                // Créer un objet JSON contenant les informations d'identification
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("prenom", prenom);
                    jsonObject.put("nom", nom);
                    jsonObject.put("pseudo", pseudo);
                    jsonObject.put("email", email);
                    jsonObject.put("password", password);
                    jsonObject.put("character_id", id);
                    jsonObject.put("limite", limite);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                Call<Void> call = serveur.register(requestBody);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("Réussi!", "!!!!!!!Compte Crée  : " + createUserViewModel.getUserMutableLiveData().getValue());
                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.fromAgreementToLogin);
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("Réussi!", t.getMessage());

                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                    Call<Void> call = serveur.register(requestBody);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("Réussi!", "!!!!!!!Compte Crée  : " + createUserViewModel.getUserMutableLiveData().getValue());
                            NavController controller = Navigation.findNavController(view);
                            controller.navigate(R.id.fromAgreementToHome);
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("Réussi!", t.getMessage());
                        }
                    });
                }
                else{
                    String erreur = getResources().getString(R.string.errAgree);
                    tvError.setText(erreur);
                }


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