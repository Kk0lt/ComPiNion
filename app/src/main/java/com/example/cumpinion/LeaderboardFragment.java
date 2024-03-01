package com.example.cumpinion;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import classes.AdapterListe;
import classes.RetrofitInstance;
import classes.User;
import classes.UserViewModel;
import interfaces.InterfaceServeur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LeaderboardFragment extends Fragment {

    //========== Variables declaration ==========
    List<User> liste = new ArrayList<>();
    List<User> listeFriends = new ArrayList<>();
    UserViewModel uvm;
    AdapterListe adapterListe;
    RecyclerView rvUsers;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        uvm = new ViewModelProvider(this).get(UserViewModel.class);
//        uvm.getUsers().observe(getActivity(), new Observer<List<User>>() {
//            @Override
//            public void onChanged(List<User> users) {
//                uvm = new ViewModelProvider(getActivity()).get(UserViewModel.class);
//            }
//
//        });

    }

    private void changeSelectedRadio(View view) {
        // Écouteur pour la sélection
        RadioGroup radioGroup = view.findViewById(R.id.rg_selectBoard);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("ovCre", "ovCrea");


        rvUsers = view.findViewById(R.id.rvUsers);
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        Call<List<User>> call = serveur.users();

        Log.d("ovCre", "2");

        call.enqueue(new Callback<List<User>>()
        {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d("on response", "worked");

                List<User> users = response.body();
                adapterListe = new AdapterListe(users);
                rvUsers.setAdapter(adapterListe);
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur");
                Log.d("erreur", t.getMessage());
            }
        });
    }
}