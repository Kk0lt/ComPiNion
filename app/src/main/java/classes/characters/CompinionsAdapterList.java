package classes.characters;

import android.graphics.Color;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cumpinion.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CompinionsAdapterList extends RecyclerView.Adapter {


    List<Compinion> liste;
    InterfaceCompinion interfaceCompinion;
    int currentPosition;

    public CompinionsAdapterList(List<Compinion> liste, InterfaceCompinion interfaceCompinion) {
        this.liste = liste;
        this.interfaceCompinion = interfaceCompinion;
        currentPosition = 0;
        this.liste.get(currentPosition).setSelected(true);
    }

    //========================================
    public CompinionsAdapterList(List<Compinion> liste){
        this.liste = liste;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.characterselect_card,parent,false); // creer une view et la rempli
        return new CompinionsAdapterList.CharactersViewHolder(view);//retourne la view que j'ai crée
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CompinionsAdapterList.CharactersViewHolder charactersViewHolder =(CompinionsAdapterList.CharactersViewHolder)holder;

        Compinion compinion = liste.get(position);

        Log.d("test", compinion.getImgUrl());
        charactersViewHolder.textView.setText(compinion.getName().toUpperCase());
        Picasso.get().load(compinion.getImgUrl()).into(charactersViewHolder.imageView);

        if(compinion.isSelected()){
            charactersViewHolder.itemView.setBackgroundColor(Color.parseColor("#0D2F54"));
            charactersViewHolder.textView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        else {
            charactersViewHolder.itemView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            charactersViewHolder.textView.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        }
    }


    @Override
    public int getItemCount() {
        return liste.size();
    }

    public void addCharacter(Compinion compinion){
        liste.add(compinion);
        notifyItemInserted(liste.size()-1);
    }

    public void editCharacter(int position, Compinion compinion){
        liste.set(position, compinion);
        notifyItemChanged(position);
    }

    public void deleteCharacter(int position){
        liste.remove(position);
        notifyItemRemoved(position);
    }

    //========================================
    public class CharactersViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public CharactersViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.characterSelectCard_iv);
            textView = itemView.findViewById(R.id.tvcompanionNameCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();

                    liste.get(currentPosition).setSelected(false);
                    Compinion compinion = liste.get(position);
                    currentPosition = position;
                    liste.get(currentPosition).setSelected(true);
                    notifyDataSetChanged();
                    interfaceCompinion.gestionClic(compinion);

                }
            });

        }

    }
}
