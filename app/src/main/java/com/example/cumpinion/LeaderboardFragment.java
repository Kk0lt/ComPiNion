package com.example.cumpinion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.cumpinion.loginFragments.LoggedUserViewModel;

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
    UsersAdapterListe amisAdapterListe;
    UsersAdapterListe blockedAdapterListe;
    RecyclerView rvUsers;
    RecyclerView rvAmis;
    RecyclerView rvBlocked;
    NavController navController;
    RadioGroup relationRadio;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

        rvBlocked = view.findViewById(R.id.rvBlocked);
        rvBlocked.setHasFixedSize(true);
        rvBlocked.setLayoutManager(new LinearLayoutManager(getContext()));

        Log.d("ovCre", "2");

        LoggedUserViewModel loggedUserViewModel = new ViewModelProvider(getActivity()).get(LoggedUserViewModel.class);

        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        getUsers(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId());
        getAmis(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId());
        getBlocked(serveur,loggedUserViewModel.getUserMutableLiveData().getValue().getId());

        relationRadio = view.findViewById(R.id.toggleRelation);
        relationRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                //Implémenter la logique de programmation pour le changement des RecyclerViews;

            }
        });

    }

    private void getUsers(InterfaceServeur serveur, int i) {
        Call<ReponseServer> call = serveur.getUsers(i);
        call.enqueue(new Callback<ReponseServer>() {
            @Override
            public void onResponse(Call<ReponseServer> call, Response<ReponseServer> response) {
                ReponseServer reponseServer = response.body();
                List<User> users = reponseServer.getUsers();
                usersAdapterListe = new UsersAdapterListe(users, navController);
                rvUsers.setAdapter(usersAdapterListe);
            }

            @Override
            public void onFailure(Call<ReponseServer> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur");
                Log.d("erreur", t.getMessage());
            }
        });
    }

    private void getAmis(InterfaceServeur s, int i) {
        Call<ReponseServer> call = s.getAmis(i);
        call.enqueue(new Callback<ReponseServer>() {@Override
            public void onResponse(Call<ReponseServer> call, Response<ReponseServer> response) {
                ReponseServer reponseServer = response.body();
                List<User> amis = reponseServer.getUsers();
                amisAdapterListe = new UsersAdapterListe(amis, navController);
                rvAmis.setAdapter(amisAdapterListe);
            }

            @Override
            public void onFailure(Call<ReponseServer> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur");
                Log.d("erreur", t.getMessage());
            }
        });
    }

    private void getBlocked(InterfaceServeur s, int i) {
        Call<ReponseServer> call = s.getBlocked(i);
        call.enqueue(new Callback<ReponseServer>() {@Override
        public void onResponse(Call<ReponseServer> call, Response<ReponseServer> response) {
            ReponseServer reponseServer = response.body();
            List<User> blocked = reponseServer.getUsers();
            blockedAdapterListe = new UsersAdapterListe(blocked, navController);
            rvBlocked.setAdapter(amisAdapterListe);
        }

            @Override
            public void onFailure(Call<ReponseServer> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur");
                Log.d("erreur", t.getMessage());
            }
        });
    }

}