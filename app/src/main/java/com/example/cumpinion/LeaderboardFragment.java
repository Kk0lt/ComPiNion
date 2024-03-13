package com.example.cumpinion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import classes.ReponseServer;
import classes.UsersAdapterListe;
import classes.RetrofitInstance;
import classes.User;
import classes.UserViewModel;
import interfaces.InterfaceServeur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LeaderboardFragment extends Fragment {

    //========== Variables declaration ==========
    UsersAdapterListe usersAdapterListe;
    RecyclerView rvUsers;
    RecyclerView rvAmis;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void changeSelectedRadio(View view) {
        // Écouteur pour la sélection
        RadioGroup radioGroup = view.findViewById(R.id.rg_selectBoard);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(radioGroup.getCheckedRadioButtonId() == -1){
                rvUsers.setVisibility(rvUsers.VISIBLE);
                rvAmis.setVisibility(rvAmis.INVISIBLE);
            } else {
                rvUsers.setVisibility(rvUsers.INVISIBLE);
                rvAmis.setVisibility(rvAmis.VISIBLE);
            }
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

        rvAmis = view.findViewById(R.id.rvAmis);
        rvAmis.setHasFixedSize(true);
        rvAmis.setLayoutManager(new LinearLayoutManager(getContext()));

        Log.d("ovCre", "2");

        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        getUsers(serveur);

    }

    private void getUsers(InterfaceServeur serveur) {
        Call<ReponseServer> call = serveur.getUsers(1);
        call.enqueue(new Callback<ReponseServer>() {
            @Override
            public void onResponse(Call<ReponseServer> call, Response<ReponseServer> response) {
                ReponseServer reponseServer = response.body();
                List<User> users = reponseServer.getUsers();
                usersAdapterListe = new UsersAdapterListe(users);
                rvUsers.setAdapter(usersAdapterListe);
            }

            @Override
            public void onFailure(Call<ReponseServer> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur");
                Log.d("erreur", t.getMessage());
            }
        });
    }

//    private void getAmis(InterfaceServeur s) {
//        Call<ReponseServer> call = s.getAmis();
//        call.enqueue(new Callback<ReponseServer>() {
//            @Override
//            public void onResponse(Call<ReponseServer> call, Response<ReponseServer> response) {
//                ReponseServer reponseServer = response.body();
//                List<User> amis = reponseServer.getAmis();
//                amisAdapterListe = new UsersAdapterListe(amis);
//                rvAmis.setAdapter(amisAdapterListe);
//            }
//
//            @Override
//            public void onFailure(Call<ReponseServer> call, Throwable t) {
//                Log.d("erreur", "onFailure Erreur");
//                Log.d("erreur", t.getMessage());
//            }
//        });
//    }
}