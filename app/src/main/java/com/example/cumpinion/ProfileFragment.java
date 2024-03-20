package com.example.cumpinion;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import classes.ReponseServer;
import classes.RetrofitInstance;
import classes.User;
import classes.UserResponseServer;
import interfaces.InterfaceServeur;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    //public DrawerLayout drawerLayout;
    //public ActionBarDrawerToggle actionBarDrawerToggle;

    Button btAdd, btBlock;
    User user = new User();

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvPseudo, nbMerit, nbStreak;
        ImageView imgProfile;

        tvPseudo = view.findViewById(R.id.tvProfile_ProfileFragment);
        nbMerit = view.findViewById(R.id.merit_profileFragment);
        nbStreak = view.findViewById(R.id.serie_profileFragment);
        imgProfile = view.findViewById(R.id.ivProfile_ProfileFragment);
        btAdd = view.findViewById(R.id.btFriend_ProfileFragment);
        btBlock = view.findViewById(R.id.btBlock_ProfileFragment);

        Bundle bundle = getArguments();
        int idSelectedUser = bundle.getInt("idSelectedUser");

        Call<UserResponseServer> call = serveur.user(idSelectedUser);
        call.enqueue(new Callback<UserResponseServer>() {
            @Override
            public void onResponse(Call<UserResponseServer> call, Response<UserResponseServer> response) {
                UserResponseServer reponseServer = response.body();
                User user = reponseServer.getUser();
                Log.d("Selected User : ", user.getNom());
                tvPseudo.setText(user.getPseudo());
                nbMerit.setText(String.valueOf(user.getMerite())); // Convert to string
                nbStreak.setText(String.valueOf(user.getJours())); // Convert to string

            }

            @Override
            public void onFailure(Call<UserResponseServer> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });




//      Pour l'image, le bundle va renvoyer l'url présente dans la carte.

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