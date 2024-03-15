package com.example.cumpinion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        tvPseudo.setText(loggedUserViewModel.getUserMutableLiveData().getValue().getPseudo());
        nbMerit.setText(String.valueOf(loggedUserViewModel.getUserMutableLiveData().getValue().getMerite()));
        nbStreak.setText(String.valueOf(loggedUserViewModel.getUserMutableLiveData().getValue().getJours()));
        int companionId = loggedUserViewModel.getUserMutableLiveData().getValue().getCompanion_id();
        getImg(companionId, new ImageCallback() {
                        @Override
                        public void onImageLoaded(String imageUrl) {
                            Picasso.get().load(imageUrl).into(imgProfile);
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