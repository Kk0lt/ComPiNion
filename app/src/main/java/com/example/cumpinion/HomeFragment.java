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

import java.util.ArrayList;
import java.util.List;

import classes.ReponseServer;
import classes.RetrofitInstance;
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
    User user;
    TextView tvPseudo, nbMerit, nbStreak;
    ImageView imgProfile;
    String url;
    Button btSmoke;
    RecyclerView rvStreaks;

//    public DrawerLayout drawerLayout;
//    public ActionBarDrawerToggle actionBarDrawerToggle;
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
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LoggedUserViewModel loggedUserViewModel = new ViewModelProvider(getActivity()).get(LoggedUserViewModel.class);

        Log.d("Réussi!", "Utilisateur : " + loggedUserViewModel.getUserMutableLiveData().getValue().getPrenom());

        tvPseudo = view.findViewById(R.id.tvHome);
        nbMerit = view.findViewById(R.id.merit);
        nbStreak = view.findViewById(R.id.serie);
        imgProfile = view.findViewById(R.id.ivHome);
        rvStreaks = view.findViewById(R.id.rvStreaks);

        tvPseudo.setText(loggedUserViewModel.getUserMutableLiveData().getValue().getPseudo());
        nbMerit.setText(String.valueOf(loggedUserViewModel.getUserMutableLiveData().getValue().getMerite()));
        nbStreak.setText(String.valueOf(loggedUserViewModel.getUserMutableLiveData().getValue().getJours()));
        int companionId = loggedUserViewModel.getUserMutableLiveData().getValue().getCompanion_id();
        btSmoke = view.findViewById(R.id.btnSmoked);
        getImg(companionId, new ImageCallback() {
                        @Override
                        public void onImageLoaded(String imageUrl) {
                            Picasso.get().load(imageUrl).into(imgProfile);
                        }
                    });

        btSmoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Comment ça va aujourd'hui?");
                builder.setPositiveButton("J'ai fumé.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder bSmoked = new AlertDialog.Builder(getContext());
                        bSmoked.setTitle("Dommage, mais ne te décourage pas!");
                        if(loggedUserViewModel.getUserMutableLiveData().getValue().getLimite()-1 < 0) {
                            bSmoked.setMessage("Tu as écoulé ta limite de cigarette pour la journée. " +
                                    "Nous reprenons la série du début, mais continue tes efforts pour battre ton record!");
                        } else {
                            bSmoked.setMessage("N'abandonne pas! Tu peux encore améliorer ta série et ton score!" +
                                    "\n" + (loggedUserViewModel.getUserMutableLiveData().getValue().getLimite()-1) + "/" + loggedUserViewModel.getUserMutableLiveData().getValue().getLimite());
                        }
                        bSmoked.setPositiveButton("Très bien", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "La mise à jour a été effectuée avec succès.", Toast.LENGTH_LONG).show();
                                int i = loggedUserViewModel.getUserMutableLiveData().getValue().getLimite()-1;
                                if(i < 0) {
                                    loggedUserViewModel.setUserStreak(0);
                                } else {

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
                builder.setNegativeButton("Je n'ai pas fumé!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Bravo!", Toast.LENGTH_LONG).show();
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

    private String getImg(int id, ImageCallback callback) {
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        Call<CompinionsReponseServer> call = serveur.getCompinion(id);
        call.enqueue(new Callback<CompinionsReponseServer>() {
            @Override
            public void onResponse(Call<CompinionsReponseServer> call, Response<CompinionsReponseServer> response) {
                CompinionsReponseServer reponseServer = response.body();
                List<Compinion> compinions = reponseServer.getCompinionList();
                for (Compinion compinion : compinions) {
                    if (compinion.getId() == id) {
                        String url = compinion.getImgUrl();
                        Picasso.get().load(compinion.getImgUrl()).into(imgProfile);
//                        callback.onImageLoaded(url);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<CompinionsReponseServer> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur character list");
                Log.d("erreur", t.getMessage());
            }
        });

        return url;
    }

    public interface ImageCallback {
        void onImageLoaded(String imageUrl);
    }

    public interface UserCallback {
        void onUserLoaded(User user);
    }

}