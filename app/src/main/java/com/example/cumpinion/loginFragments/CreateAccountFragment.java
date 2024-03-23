package com.example.cumpinion.loginFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cumpinion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import classes.RetrofitInstance;
import classes.User;
import interfaces.InterfaceServeur;


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

        createAccountinViewModel(view, etPrenom, etNom, etPseudo, etEmail, etPassword, etConfirmPassword, btCreate, serveur);
        // Inflate the layout for this fragment
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        // Rétablir la visibilité de la barre de navigation
        bottomNavigationView.setVisibility(View.GONE);

    }

    private void createAccountinViewModel(@NonNull View view, EditText etPrenom, EditText etNom, EditText etPseudo, EditText etEmail, EditText etPassword, EditText etConfirmPassword,
                                          Button btCreate, InterfaceServeur serveur) {
        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean valide = true;

                if (TextUtils.isEmpty(etPrenom.getText().toString())){
                    etPrenom.setError("Entrez un prénom");
                    valide = false;
                }
                if (TextUtils.isEmpty(etNom.getText().toString())){
                    etNom.setError("Entrez un nom");
                    valide = false;
                }
                if (TextUtils.isEmpty(etPseudo.getText().toString())){
                    etPseudo.setError("Entrez un Pseudo");
                    valide = false;
                }
                if (TextUtils.isEmpty(etEmail.getText().toString())){
                    etEmail.setError("Entrez un email");
                    valide = false;
                }
                if (TextUtils.isEmpty(etPassword.getText().toString())){
                    etPassword.setError("Entrez un mot de passe");
                    valide = false;
                }
                if (TextUtils.isEmpty(etConfirmPassword.getText().toString())){
                    etConfirmPassword.setError("Confirmez votre mot de passe");
                    valide = false;
                }
                else if (valide = true) {

                    String prenom = etPrenom.getText().toString();
                    String nom = etNom.getText().toString();
                    String pseudo = etPseudo.getText().toString();
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    String confirmPassword = etConfirmPassword.getText().toString();
                    CreateUserViewModel createUserViewModel = new ViewModelProvider(getActivity()).get(CreateUserViewModel.class);

                    if (confirmPassword.equals(password)) {
                        User newUser = new User(nom, prenom, email, password, pseudo, 1, 0);
                        createUserViewModel.addUser(newUser);
                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.fromCreateAccountToLimitSelect);
                    } else {
                        Toast.makeText(getContext(), "Les mots de passe ne concordent pas", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}