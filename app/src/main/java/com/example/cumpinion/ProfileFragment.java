package com.example.cumpinion;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import classes.ReponseServer;
import classes.RetrofitInstance;
import classes.User;
import classes.UsersAdapterListe;
import classes.characters.Compinion;
import classes.characters.CompinionsReponseServer;
import interfaces.InterfaceServeur;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    //public DrawerLayout drawerLayout;
    //public ActionBarDrawerToggle actionBarDrawerToggle;
    TextView tvPseudo, nbMerit, nbStreak;
    ImageView imgProfile;
    Button btAdd, btBlock;
    User user;

    public ProfileFragment() {
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
        User user = getUser(serveur, 1); //Normalement, le bundle va retourner le id du user logged in

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvPseudo = view.findViewById(R.id.tvProfile);
        nbMerit = view.findViewById(R.id.merit);
        nbStreak = view.findViewById(R.id.serie);
        imgProfile = view.findViewById(R.id.ivProfile);
        btAdd = view.findViewById(R.id.btFriend);
        btBlock = view.findViewById(R.id.btBlock);

        tvPseudo.setText(user.getPseudo());
        nbMerit.setText(user.getMerite());
        nbStreak.setText(user.getJours());
        //Pour l'image, le bundle va renvoyer l'url présente dans la carte.


//      drawerLayout = view.findViewById(R.id.drawer);
//      actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.nav_open, R.string.nav_close);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private User getUser(InterfaceServeur s, int id) {
        Call<ReponseServer> call = s.user(id);
        call.enqueue(new Callback<ReponseServer>() {
            @Override
            public void onResponse(Call<ReponseServer> call, Response<ReponseServer> response) {
                List<User> luser = (List<User>) response.body();
                user = luser.get(0);
            }

            @Override
            public void onFailure(Call<ReponseServer> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur");
                Log.d("erreur", t.getMessage());
            }

        });
        return user;
    }

    private void block(InterfaceServeur s, User user, int id2) {
        int idLoggedUser = user.getId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user1_id", idLoggedUser);
            jsonObject.put("user2_id", id2);
            jsonObject.put("relation", "blocked");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<Void> c = s.block(idLoggedUser, id2, requestBody);
        c.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("success", "Bravo ça marche!");
                //NavController controller = Navigation.findNavController(view);
                //controller.navigate(R.id.fromAgreementToHome);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("erreur", "onFailure Erreur");
                Log.d("erreur", t.getMessage());
            }
        });
    }


}