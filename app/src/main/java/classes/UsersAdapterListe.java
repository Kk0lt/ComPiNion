package classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cumpinion.R;

import java.util.List;

public class UsersAdapterListe extends RecyclerView.Adapter {

    List<User> liste;


    //========================================
    public UsersAdapterListe(List<User> liste){
        this.liste = liste;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_carte,parent,false); // creer une view et la rempli
        return new UsersViewHolder(view);//retourne la view que j'ai crée
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // apres avoir gonflé la view il va les lier
        UsersViewHolder usersViewHolder =(UsersViewHolder)holder;

        User user = liste.get(position);
        String fulllName =user.getNom() + " " + user.getPrenom();

        String pseudo = "@"+user.getPseudo() ;
        usersViewHolder.tvPseudo.setText(pseudo);


    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public void addUser(User user){
        liste.add(user);
        notifyItemInserted(liste.size()-1);
    }

    public void editUser(int position, User user){
        liste.set(position, user);
        notifyItemChanged(position);
    }

    public void deleteUser(int position){
        liste.remove(position);
        notifyItemRemoved(position);
    }

    //========================================
    public class UsersViewHolder extends RecyclerView.ViewHolder{

        TextView tvNom, tvPseudo, tvXp;
        ImageView ivCompanionImage;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            //tvNom = itemView.findViewById(R.id.tvNom);
            tvPseudo = itemView.findViewById(R.id.tvPseudo);
            tvXp = itemView.findViewById(R.id.tvXp);
            ivCompanionImage = itemView.findViewById(R.id.ivCompanionImage);



        }

    }
}
