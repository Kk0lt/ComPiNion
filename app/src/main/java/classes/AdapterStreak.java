package classes;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        View view = inflater.inflate(R.layout.layout_streak,parent,false);
        return new MonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MonViewHolder monViewHolder = (MonViewHolder) holder;
        monViewHolder.tvMinStreak.setText("" + liste.get(position).getJours());
        monViewHolder.tvDateDebS.setText(liste.get(position).getDateDeb().toString());
        monViewHolder.tvDateFinS.setText(liste.get(position).getDateFin().toString());

    }

    @Override
    public int getItemCount() {
        return liste.size();
    }


    public class MonViewHolder extends RecyclerView.ViewHolder{


        //DECLARER DES VARIABLES

        TextView tvDateDebS, tvDateFinS, tvMinStreak;

        public MonViewHolder(@NonNull View ItemView){
            super(ItemView);
            tvDateDebS = ItemView.findViewById(R.id.tvDateDebS);
            tvDateFinS = ItemView.findViewById(R.id.tvDateFinS);
            tvMinStreak = ItemView.findViewById(R.id.tvMinStreak);





        }
    }
}
