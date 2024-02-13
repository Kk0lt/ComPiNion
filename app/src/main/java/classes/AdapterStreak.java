package classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cumpinion.R;

import java.util.List;

public class AdapterStreak extends RecyclerView.Adapter{

    List<Streak> liste;

    public AdapterStreak(List<Streak> l)    {
        this.liste = l;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_carte,parent,false);
        return new MonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class MonViewHolder extends RecyclerView.ViewHolder{


        //DECLARER DES VARIABLES

        public MonViewHolder(@NonNull View ItemView){
            super(ItemView);


        }
    }
}
