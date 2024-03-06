package com.example.cumpinion.loginFragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cumpinion.R;

import java.util.List;

import classes.RetrofitInstance;

import classes.characters.Compinion;
import classes.characters.CompinionsAdapterList;
import classes.characters.CompinionsReponseServer;
import classes.characters.InterfaceCompinion;
import interfaces.InterfaceServeur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class limitselectionFragment extends Fragment implements InterfaceCompinion {

    RecyclerView rvCharacters;
    CompinionsAdapterList compinionsAdapterList;

    public limitselectionFragment() {
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
        return inflater.inflate(R.layout.fragment_limitselection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btNext = view.findViewById(R.id.btNext_limitSelectionFragment);
        CreateUserViewModel createUserViewModel = new ViewModelProvider(getActivity()).get(CreateUserViewModel.class);
        NavController controller = Navigation.findNavController(view);
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);

        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
//                setEnabled(false);
//                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        rvCharacters = view.findViewById(R.id.rvCharacters);
        rvCharacters.setHasFixedSize(true);
        rvCharacters.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));

        showSelectableCompanions(serveur);

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.fromLimitSelectToAgreement);
            }
        });

    }

    //Methode pour afficher les companions de la base de donnés
    private void showSelectableCompanions(InterfaceServeur serveur) {
        Call<CompinionsReponseServer> call = serveur.getAllCompinions();
        call.enqueue(new Callback<CompinionsReponseServer>() {
            @Override
            public void onResponse(Call<CompinionsReponseServer> call, Response<CompinionsReponseServer> response) {
                Log.d("on response", "response character list");
                CompinionsReponseServer reponseServer = response.body();
                List<Compinion> compinions = reponseServer.getCompinionList();
                compinionsAdapterList = new CompinionsAdapterList(compinions);
                rvCharacters.setAdapter(compinionsAdapterList);
            }

            @Override
            public void onFailure(Call<CompinionsReponseServer> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur character list");
                Log.d("erreur", t.getMessage());
            }
        });
    }

    @Override
    public void gestionClic(Compinion compinion) {
        CreateUserViewModel createUserViewModel = new ViewModelProvider(getActivity()).get(CreateUserViewModel.class);
        createUserViewModel.getUserMutableLiveData().getValue().setCompanion_id(compinion.getId());
    }
}