package com.example.cumpinion;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cumpinion.loginFragments.CreateUserViewModel;
import com.example.cumpinion.loginFragments.LoggedUserViewModel;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import classes.ReponseServer;
import classes.RetrofitInstance;
import classes.Streak;
import classes.StreakReponseServer;
import classes.User;
import classes.UsersAdapterListe;
import classes.characters.Compinion;
import classes.characters.CompinionsReponseServer;
import interfaces.InterfaceServeur;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    TextView tvPseudo, nbMerit, nbStreak;
    ImageView imgProfile;
    String url;
    Button btSmoke;
    RecyclerView rvStreaks;
    Streak streak;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Appel du serveur
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);

        //Layout
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //On va chercher l'usager connecté
        LoggedUserViewModel loggedUserViewModel = new ViewModelProvider(getActivity()).get(LoggedUserViewModel.class);

        //get des éléments de mon xml
        tvPseudo = view.findViewById(R.id.tvHome);
        nbMerit = view.findViewById(R.id.merit);
        nbStreak = view.findViewById(R.id.serie);
        imgProfile = view.findViewById(R.id.ivHome);
        rvStreaks = view.findViewById(R.id.rvStreaks);
        btSmoke = view.findViewById(R.id.btnSmoked);
        getStreakEnCours(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId());

        //Connexion des éléments du xml avec les données de l'utilisateur
        tvPseudo.setText(loggedUserViewModel.getUserMutableLiveData().getValue().getPseudo());
        nbMerit.setText(String.valueOf(loggedUserViewModel.getUserMutableLiveData().getValue().getMerite()));
        nbStreak.setText(String.valueOf(loggedUserViewModel.getUserMutableLiveData().getValue().getJours()));
        Picasso.get().load(loggedUserViewModel.getUserMutableLiveData().getValue().getCompanion_url()).into(imgProfile);

        //Modals pour le bouton fumé
        btSmoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Comment ça va aujourd'hui?");

                //J'ai fumé
                builder.setPositiveButton("J'ai fumé.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder bSmoked = new AlertDialog.Builder(getContext());
                        bSmoked.setTitle("Dommage, mais ne te décourage pas!");
                        //Si ma limite est écoulé texte
                        if(loggedUserViewModel.getUserMutableLiveData().getValue().getLimite()-1 < 0) {
                            bSmoked.setMessage("Tu as écoulé ta limite de cigarette pour la journée. " +
                                    "Nous reprenons la série du début, mais continue tes efforts pour battre ton record!");
                        //Si ma limite n'est pas écoulée texte
                        } else {
                            bSmoked.setMessage("N'abandonne pas! Tu peux encore améliorer ta série et ton score!" +
                                    "\n" + (loggedUserViewModel.getUserMutableLiveData().getValue().getLimite()-1) + "/" + loggedUserViewModel.getUserMutableLiveData().getValue().getLimite());
                        }

                        //Confirmation
                        bSmoked.setPositiveButton("Très bien", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int i = loggedUserViewModel.getUserMutableLiveData().getValue().getLimite()-1;
                                //Si ma limite est écoulée, on choisit une nouvelle limite et on update la streak
                                if(i < 0) {
                                    updateStreak(loggedUserViewModel, 0);
                                    resetStreak(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId());
                                    AlertDialog.Builder bLimite = new AlertDialog.Builder(getContext());
                                    bLimite.setTitle("Choisissez une nouvelle limite de cigarette par jours");
                                    bLimite.setPositiveButton("1", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateLimite(loggedUserViewModel, 1);
                                            Toast.makeText(getContext(), "Votre nouvelle série est commencée! Bonne chance!", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    bLimite.setNegativeButton("2", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateLimite(loggedUserViewModel,2);
                                            Toast.makeText(getContext(), "Votre nouvelle série est commencée! Bonne chance!", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    bLimite.setNeutralButton("3", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateLimite(loggedUserViewModel, 3);
                                            Toast.makeText(getContext(), "Votre nouvelle série est commencée! Bonne chance!", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    AlertDialog alertLimite = bLimite.create();
                                    alertLimite.show();
                                //Si ma limite n'est pas écoulée, on fait juste mettre à jour la limite
                                } else {
                                    updateLimite(loggedUserViewModel, i);
                                }
                            }
                        });

                        bSmoked.setNegativeButton("C'est une erreur.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "La mise à jour a été annulée.", Toast.LENGTH_LONG).show();
                            }
                        });
                        AlertDialog alertSmoke = bSmoked.create();
                        alertSmoke.show();
                    }
                });

                //Je n'ai pas fumé aujourd'hui
                builder.setNegativeButton("Je n'ai pas fumé!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LocalDate localDate = LocalDate.now();
                        getStreakEnCours(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId());
                        LocalDate diff = streak.getStart_date().plusDays(loggedUserViewModel.getUserMutableLiveData().getValue().getJours());
                        if(localDate.isAfter(diff)) {
                            Toast.makeText(getContext(), "Bravo!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Bravo! Une journée de plus à votre série!", Toast.LENGTH_LONG).show();
                            updateStreak(loggedUserViewModel, loggedUserViewModel.getUserMutableLiveData().getValue().getJours()+1);
                        }

                    }
                });
                builder.setNeutralButton("J'y pense!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Ne lâche pas!", Toast.LENGTH_LONG).show();
                        int i = loggedUserViewModel.getUserMutableLiveData().getValue().getMerite();
                        updateMerite(loggedUserViewModel, i+1);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return view;
    }

    private void getStreakEnCours(InterfaceServeur serveur, int i) {
        Call<StreakReponseServer> call = serveur.getStreak(i);
        call.enqueue(new Callback<StreakReponseServer>() {
            @Override
            public void onResponse(Call<StreakReponseServer> call, Response<StreakReponseServer> response) {
                StreakReponseServer reponseServer = response.body();
                streak = reponseServer.getStreak();
            }

            @Override
            public void onFailure(Call<StreakReponseServer> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur");
                Log.d("erreur", t.getMessage());
            }
        });
    }

    private void resetStreak(InterfaceServeur serveur, int i) {
        Call<Void> call = serveur.endStreak(i);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "La mise à jour a été effectuée avec succès.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur");
                Log.d("erreur", t.getMessage());
            }
        });
    }

    private static void updateMerite(LoggedUserViewModel loggedUserViewModel, int i) {
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        Call<Void> call = serveur.updateMerite(loggedUserViewModel.getUserMutableLiveData().getValue().getId(), i);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loggedUserViewModel.setUserMerit(i);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("failed", "Failed: "+ t.getMessage());
            }
        });
    }

    private static void updateStreak(LoggedUserViewModel loggedUserViewModel, int i) {
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        Call<Void> call = serveur.updateJours(loggedUserViewModel.getUserMutableLiveData().getValue().getId(), i);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loggedUserViewModel.setUserStreak(i);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("failed", "Failed: "+ t.getMessage());
            }
        });
    }

    private static void updateLimite(LoggedUserViewModel loggedUserViewModel, int i) {
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        Call<Void> call = serveur.updateLimite(loggedUserViewModel.getUserMutableLiveData().getValue().getId(), i);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loggedUserViewModel.setUserLimit(i);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("failed", "Failed: "+ t.getMessage());
            }
        });
    }

}