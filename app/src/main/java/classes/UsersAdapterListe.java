package classes;

import android.app.Activity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cumpinion.LeaderboardFragment;
import com.example.cumpinion.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import classes.characters.Compinion;
import classes.characters.CompinionsReponseServer;
import interfaces.InterfaceServeur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersAdapterListe extends RecyclerView.Adapter {

    private List<User> liste;
    private String url;

    public UsersAdapterListe(List<User> liste) {
        this.liste = liste;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_carteuser,parent,false);
        return new UsersViewHolder(view);//retourne la view que j'ai crée
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UsersViewHolder usersViewHolder = (UsersViewHolder) holder;
        User user = liste.get(position);

        String pseudo = "@" + user.getPseudo();
        usersViewHolder.tvPseudo.setText(pseudo);

        int xp = user.getJours();
        String experience = xp + " jours";
        usersViewHolder.tvXp.setText(experience);

        int cid = user.getCompanion_id();
        getImg(cid, new ImageCallback() {
            @Override
            public void onImageLoaded(String imageUrl) {
                Picasso.get().load(imageUrl).into(usersViewHolder.ivCompanionImage);
            }
        });

    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {
        TextView tvPseudo, tvXp;
        ImageView ivCompanionImage;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPseudo = itemView.findViewById(R.id.tvPseudo);
            tvXp = itemView.findViewById(R.id.tvXp);
            ivCompanionImage = itemView.findViewById(R.id.ivCompanionImage);
        }

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

}
