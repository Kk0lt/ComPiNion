package classes.streaks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cumpinion.R;

import java.text.SimpleDateFormat;
import java.util.List;

import classes.streaks.Streak;

public class StreaksAdapterList extends RecyclerView.Adapter {

    private List<Streak> liste;

    public StreaksAdapterList(List<Streak> liste) {
        this.liste = liste;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_streak,parent,false);
        return new StreaksViewHolder(view);//retourne la view que j'ai crée
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        StreaksViewHolder streaksViewHolder = (StreaksViewHolder) holder;
        Streak streak = liste.get(position);

        if (streak.getStartDate() != null && streak.getEndDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String startDate = sdf.format(streak.getStartDate());
            String endDate = sdf.format(streak.getEndDate());
            long days = Math.abs((streak.getEndDate().getTime() - streak.getStartDate().getTime()) / (1000 * 60 * 60 * 24));

            streaksViewHolder.tvJours.setText(String.valueOf(days));
            streaksViewHolder.tvStart.setText(startDate);
            streaksViewHolder.tvEnd.setText(endDate);
        } else {
            streaksViewHolder.tvJours.setText("0");
            streaksViewHolder.tvStart.setText("Erreur");
            streaksViewHolder.tvEnd.setText("En cours");
        }

    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public class StreaksViewHolder extends RecyclerView.ViewHolder {
        TextView tvJours, tvStart, tvEnd;

        public StreaksViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStart = itemView.findViewById(R.id.tvStart);
            tvJours = itemView.findViewById(R.id.tvJours);
            tvEnd = itemView.findViewById(R.id.tvEnd);
        }
    }

    public void setStreaks(List<Streak> lstreaks) {
        this.liste = lstreaks;
        notifyItemInserted(liste.size()-1);
    }

}
