package com.example.cumpinion.loginFragments;

import android.graphics.PorterDuff;
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
import android.widget.ImageView;

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
    int selectedLimit = 0;

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
        ImageView ic_0, ic_1, ic_2, ic_3;
        CreateUserViewModel createUserViewModel = new ViewModelProvider(getActivity()).get(CreateUserViewModel.class);
        NavController controller = Navigation.findNavController(view);
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);


        ic_0 = view.findViewById(R.id.ivIcon0);
        ic_1 = view.findViewById(R.id.ivIcon1);
        ic_2 = view.findViewById(R.id.ivIcon2);
        ic_3 = view.findViewById(R.id.ivIcon3);

        // Réinitialisez la couleur de contour pour toutes les images
        resetTint(ic_0, ic_1, ic_2, ic_3);

        // Ajoutez des onClickListeners pour chaque image
        limitSelection(ic_0, ic_1, ic_2, ic_3);


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
                CreateUserViewModel createUserViewModel = new ViewModelProvider(getActivity()).get(CreateUserViewModel.class);
                createUserViewModel.setUserLimit(selectedLimit);

                NavController controller = Navigation.findNavController(view);
                controller.navigate(R.id.fromLimitSelectToAgreement);
            }
        });

    }

    /*----------------------- Methodes privés -----------------------*/


    /**
     * Méthode pour réinitialiser la couleur de contour de toutes les images
     * */
    private void limitSelection(ImageView ic_0, ImageView ic_1, ImageView ic_2, ImageView ic_3) {
        ic_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTint(ic_0, ic_1, ic_2, ic_3);
                ic_0.setColorFilter(getResources().getColor(R.color.hardBlue), PorterDuff.Mode.SRC_IN);
                selectedLimit = 0;
            }
        });

        ic_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTint(ic_0, ic_1, ic_2, ic_3);
                ic_1.setColorFilter(getResources().getColor(R.color.hardBlue), PorterDuff.Mode.SRC_IN);
                selectedLimit = 1;

            }
        });

        ic_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTint(ic_0, ic_1, ic_2, ic_3);
                ic_2.setColorFilter(getResources().getColor(R.color.hardBlue), PorterDuff.Mode.SRC_IN);
                selectedLimit = 2;

            }
        });

        ic_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTint(ic_0, ic_1, ic_2, ic_3);
                ic_3.setColorFilter(getResources().getColor(R.color.hardBlue), PorterDuff.Mode.SRC_IN);
                selectedLimit = 3;

            }
        });
    }

    /**
     * Méthode pour réinitialiser la couleur de contour de toutes les images
     */
    private void resetTint(ImageView... imageViews) {
        for (ImageView imageView : imageViews) {
            imageView.clearColorFilter(); // Efface tout filtre de couleur précédent
            imageView.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_IN); // Applique la couleur par défaut avec le mode SRC_IN
        }
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
                compinionsAdapterList = new CompinionsAdapterList(compinions, limitselectionFragment.this);
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