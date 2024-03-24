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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cumpinion.loginFragments.LoggedUserViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import classes.ReponseServer;
import classes.RetrofitInstance;
import classes.StringReponseServer;
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
        getRelationBetweenUsers(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId(), idSelectedUser);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(relation != null) {
                    if (relation.equals("friend")) {
                        unfriend(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId(), idSelectedUser, tvPseudo);
                        getRelationBetweenUsers(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId(), idSelectedUser);
                    }
                }
                else {
                    befriend(serveur,loggedUserViewModel.getUserMutableLiveData().getValue().getId(), idSelectedUser, tvPseudo);
                    getRelationBetweenUsers(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId(), idSelectedUser);
                }
            }
        });

        btBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(relation != null) {
                    if (relation.equals("blocked")) {
                        unblock(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId(), idSelectedUser, tvPseudo);
                        getRelationBetweenUsers(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId(), idSelectedUser);
                    }
                }
                else {
                    block(serveur,loggedUserViewModel.getUserMutableLiveData().getValue().getId(), idSelectedUser, tvPseudo);
                    getRelationBetweenUsers(serveur, loggedUserViewModel.getUserMutableLiveData().getValue().getId(), idSelectedUser);
                }
            }
        });

        return view;
    }

    private void befriend(InterfaceServeur serveur, int a, int i, TextView pseudo) {
        NavController controller = NavHostFragment.findNavController(this);
        Call<Void> call = serveur.friend(a, i);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "Vous avez commencé à suivre " + pseudo.getText(), Toast.LENGTH_LONG).show();
                controller.navigate(R.id.fromProfileToLeaderboard);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });
    }

    private void unfriend(InterfaceServeur serveur, int a, int i, TextView pseudo) {
        NavController controller = NavHostFragment.findNavController(this);
        Call<Void> call = serveur.unfriend(a, i);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "Vous avez cessé de suivre " + pseudo.getText(), Toast.LENGTH_LONG).show();
                controller.navigate(R.id.fromProfileToLeaderboard);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });
    }

    private void block(InterfaceServeur serveur, int a, int i, TextView pseudo) {
        NavController controller = NavHostFragment.findNavController(this);
        Call<Void> call = serveur.block(a, i);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "Vous avez bloqué " + pseudo.getText(), Toast.LENGTH_LONG).show();
                controller.navigate(R.id.fromProfileToLeaderboard);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });
    }

    private void unblock(InterfaceServeur serveur, int a, int i, TextView pseudo) {
        NavController controller = NavHostFragment.findNavController(this);
        Call<Void> call = serveur.unblock(a, i);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "Vous avez débloqué " + pseudo.getText(), Toast.LENGTH_LONG).show();
                controller.navigate(R.id.fromProfileToLeaderboard);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });
    }

    private void getRelationBetweenUsers(InterfaceServeur serveur, int i, int idSelectedUser) {
        Call<StringReponseServer> call = serveur.relation(i, idSelectedUser);
        call.enqueue(new Callback<StringReponseServer>() {
            @Override
            public void onResponse(Call<StringReponseServer> call, Response<StringReponseServer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    relation = response.body().getData();
                } else {
                    relation = null;
                }
                updateUI();
            }

            @Override
            public void onFailure(Call<StringReponseServer> call, Throwable t) {
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

    private void updateUI() {
        String unfriend = getResources().getString(R.string.remove_friend);
        String friend = getResources().getString(R.string.add_friend);
        String block = getResources().getString(R.string.block);
        String unblock = getResources().getString(R.string.unblock);

        if (relation != null) {
            if ("friend".equals(relation)) {
                btAdd.setVisibility(View.VISIBLE);
                btBlock.setVisibility(View.INVISIBLE);
                btAdd.setText(unfriend);
            } else if ("blocked".equals(relation)) {
                btAdd.setVisibility(View.INVISIBLE);
                btBlock.setVisibility(View.VISIBLE);
                btBlock.setText(unblock);
            }
        } else {
            btAdd.setVisibility(View.VISIBLE);
            btBlock.setVisibility(View.VISIBLE);
            btAdd.setText(friend);
            btBlock.setText(block);
        }
    }

}