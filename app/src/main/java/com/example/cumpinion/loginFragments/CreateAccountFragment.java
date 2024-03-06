package com.example.cumpinion.loginFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cumpinion.R;

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

    }

    private void createAccountinViewModel(@NonNull View view, EditText etPrenom, EditText etNom, EditText etPseudo, EditText etEmail, EditText etPassword, EditText etConfirmPassword,
                                          Button btCreate, InterfaceServeur serveur) {
        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prenom = etPrenom.getText().toString();
                String nom = etNom.getText().toString();
                String pseudo = etPseudo.getText().toString();
                String email  = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                CreateUserViewModel createUserViewModel = new ViewModelProvider(getActivity()).get(CreateUserViewModel.class);

                if(confirmPassword.equals(password)){
                    User newUser = new User(prenom, nom, pseudo, email, password, 1);
                    createUserViewModel.addUser(newUser);
                }
                else{
                    Toast.makeText(getContext(), "Les mots de passe ne concordent pas", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }
}