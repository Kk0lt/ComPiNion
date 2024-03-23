package com.example.cumpinion;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cumpinion.loginFragments.LoggedUserViewModel;

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

    Button btAdd, btBlock;
    String relation;

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
        LoggedUserViewModel loggedUserViewModel = new ViewModelProvider(requireActivity()).get(LoggedUserViewModel.class);
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
//      Pour l'image, le bundle va renvoyer l'url présente dans la carte.

        Bundle bundle = getArguments();
        int idSelectedUser = bundle.getInt("idSelectedUser");

        getUser(serveur, idSelectedUser, tvPseudo, nbMerit, nbStreak);
        getRelationBetweenUsers(serveur, loggedUserViewModel, idSelectedUser);

        if(!TextUtils.isEmpty(relation)) {
            if(relation.equalsIgnoreCase("friend")) {
                btAdd.setVisibility(View.VISIBLE);
                btBlock.setVisibility(View.INVISIBLE);
                btAdd.setText("@string/remove_friend");
            }
            else if(relation.equalsIgnoreCase("blocked")) {
                btAdd.setVisibility(View.INVISIBLE);
                btBlock.setVisibility(View.VISIBLE);
                btBlock.setText("@string/unblock");
            }
        } else {
            btAdd.setVisibility(View.VISIBLE);
            btBlock.setVisibility(View.VISIBLE);
            btAdd.setText("@string/add_friend");
            btBlock.setText("@string/block");
        }
        
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (relation.equalsIgnoreCase("friend"))
                    befriend(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId(), idSelectedUser, tvPseudo);
                else
                    unfriend(serveur,loggedUserViewModel.getUserMutableLiveData().getValue().getId(), idSelectedUser, tvPseudo);
            }
        });

        btBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void befriend(InterfaceServeur serveur, int a, int i, TextView pseudo) {
        Call<Void> call = serveur.friend(a, i);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "Vous avez commencé à suivre " + pseudo.getText(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });
    }

    private void unfriend(InterfaceServeur serveur, int a, int i, TextView pseudo) {
        Call<Void> call = serveur.unfriend(a, i);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "Vous avez cessé de suivre " + pseudo.getText(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });
    }

    private void getRelationBetweenUsers(InterfaceServeur serveur, LoggedUserViewModel loggedUserViewModel, int idSelectedUser) {
        Call<String> call = serveur.relation(loggedUserViewModel.getUserMutableLiveData().getValue().getId(), idSelectedUser);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    relation = response.body();
                } else {
                    relation = null;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });
    }

    private static void getUser(InterfaceServeur serveur, int idSelectedUser, TextView tvPseudo, TextView nbMerit, TextView nbStreak) {
        Call<UserResponseServer> call = serveur.user(idSelectedUser);
        call.enqueue(new Callback<UserResponseServer>() {
            @Override
            public void onResponse(Call<UserResponseServer> call, Response<UserResponseServer> response) {
                UserResponseServer reponseServer = response.body();
                User user = reponseServer.getUser();
                tvPseudo.setText(user.getPseudo());
                nbMerit.setText(String.valueOf(user.getMerite())); // Convert to string
                nbStreak.setText(String.valueOf(user.getJours())); // Convert to string
            }

            @Override
            public void onFailure(Call<UserResponseServer> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });
    }
}