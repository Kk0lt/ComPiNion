package com.example.cumpinion;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        //Normalement, le bundle venant du login va retourner l'id du user connecté

//        drawerLayout = view.findViewById(R.id.drawer);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.nav_open, R.string.nav_close);

        tvPseudo = view.findViewById(R.id.tvHome);
        nbMerit = view.findViewById(R.id.merit);
        nbStreak = view.findViewById(R.id.serie);
        imgProfile = view.findViewById(R.id.ivHome);
        btSmoke = view.findViewById(R.id.btnSmoked);

        User user = getUser(serveur, 2, new UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                if (user != null) {
                    tvPseudo.setText(user.getPseudo());
                    nbMerit.setText(String.valueOf(user.getMerite()));
                    nbStreak.setText(String.valueOf(user.getJours()));
                    int cid = user.getCompanion_id();
                    getImg(cid, new ImageCallback() {
                        @Override
                        public void onImageLoaded(String imageUrl) {
                            Picasso.get().load(imageUrl).into(imgProfile);
                        }
                    });
                }
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
                        if(user.getLimite()-1 == 0) {
                            bSmoked.setMessage("Tu as écoulé ta limite de cigarette pour la journée. " +
                                    "Nous reprenons la série du début, mais continue tes efforts pour battre ton record!");
                        } else {
                            bSmoked.setMessage("N'abandonne pas! Tu peux encore améliorer ta série et ton score!" +
                                    "\n" + (user.getLimite() - 1) + "/" + user.getLimite());
                        }
                        bSmoked.setPositiveButton("Très bien", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "La mise à jour a été effectuée avec succès.", Toast.LENGTH_LONG).show();
                            }
                        });

                        bSmoked.setNegativeButton("C'est une erreur.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "La mise à jour a été annulée.", Toast.LENGTH_LONG).show();
                            }
                        });
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
                        user.setMerite(user.getMerite()+1);
                    }
                });

            }
        });

        return view;
    }

    private User getUser(InterfaceServeur s, int id, final UserCallback callback) {

        Call<ReponseServer> call = s.user(id);
        call.enqueue(new Callback<ReponseServer>() {
            @Override
            public void onResponse(Call<ReponseServer> call, Response<ReponseServer> response) {
                ReponseServer reponseServer = response.body();
                List<User> userList = reponseServer.getUsers();
                User user = userList.get(0);
                callback.onUserLoaded(user);
            }

            @Override
            public void onFailure(Call<ReponseServer> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur");
                Log.d("erreur", t.getMessage());
            }

        });
        return user;
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
                        callback.onImageLoaded(url);
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