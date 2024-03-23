package classes;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cumpinion.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import classes.characters.Compinion;
import classes.characters.CompinionReponseServer;
import interfaces.InterfaceServeur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersAdapterListe extends RecyclerView.Adapter {

    private List<User> liste;
    private NavController navController;
    int idSelectedUser;

    public UsersAdapterListe(List<User> liste, NavController navController) {
        this.liste = liste;
        this.navController = navController;
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
        if (liste.isEmpty()) {

            UsersViewHolder usersViewHolder = (UsersViewHolder) holder;
            usersViewHolder.tvPseudo.setText("Cette liste est vide.");
            usersViewHolder.tvXp.setText("");
            usersViewHolder.ivCompanionImage.setVisibility(View.GONE);

        } else {

            UsersViewHolder usersViewHolder = (UsersViewHolder) holder;
            User user = liste.get(position);

            String pseudo = "@" + user.getPseudo();
            usersViewHolder.tvPseudo.setText(pseudo);

            int xp = user.getJours();
            String experience = xp + " jours";
            usersViewHolder.tvXp.setText(experience);

            Picasso.get().setLoggingEnabled(true);
            getImg(user.getId(), usersViewHolder.ivCompanionImage);
        }
    }

    @Override
    public int getItemCount() {
        if (liste.isEmpty()) {
            return 1;
        } else {
            return liste.size();
        }
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {
        TextView tvPseudo, tvXp;
        ImageView ivCompanionImage;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPseudo = itemView.findViewById(R.id.tvPseudo);
            tvXp = itemView.findViewById(R.id.tvXp);
            ivCompanionImage = itemView.findViewById(R.id.ivCompanionImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    idSelectedUser = liste.get(position).getId();
                    Bundle bundle = new Bundle();
                    bundle.putInt("idSelectedUser", idSelectedUser);

                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.fromLeaderboardToProfile, bundle);
                }
            });
        }
    }

    private void getImg(int i, ImageView imageView) {
        InterfaceServeur serveur = RetrofitInstance.getInstance().create(InterfaceServeur.class);
        Call<CompinionReponseServer> call = serveur.getCompinion(i);
        call.enqueue(new Callback<CompinionReponseServer>() {
            @Override
            public void onResponse(Call<CompinionReponseServer> call, Response<CompinionReponseServer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Compinion c = response.body().getCompinion();
                    if (c != null) {
                        String imgUrl = c.getImage();
                        Picasso.get().load(Uri.parse(imgUrl)).into(imageView);
                    } else {
                        Log.d("failed", "La réponse est null");
                    }
                } else {
                    Log.d("failed", "La réponse a échoué");
                }
            }

            @Override
            public void onFailure(Call<CompinionReponseServer> call, Throwable t) {
                Log.d("failed", "Failed: "+ t.getMessage());
            }
        });
    }
}
