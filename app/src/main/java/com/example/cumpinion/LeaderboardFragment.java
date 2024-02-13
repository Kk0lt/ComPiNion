package com.example.cumpinion;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import classes.AdapterListe;
import classes.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderboardFragment extends Fragment {
    //========== Variables declaration ==========
    List<User> liste = new ArrayList<>();
    List<User> listeFriends = new ArrayList<>();
    RecyclerView rv;
    AdapterListe adapterListe;
    Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderboardFragment newInstance(String param1, String param2) {
        LeaderboardFragment fragment = new LeaderboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        rv = view.findViewById(R.id.rvLeaderBoard);
        rv.setHasFixedSize(true);

        //rv.setLayoutManager(new LinearLayoutManager( this.getContext() , LinearLayoutManager.VERTICAL, false));
        rv.setLayoutManager(
                new LinearLayoutManager(getContext()));

        liste.add(new User("Aidoun", "Lyes", "lyesaid29@gmail.com", "12345","laidoun"));
        liste.add(new User("Fanny", "Hamel", "lyesaid29@gmail.com", "12345","fhamel"));
        liste.add(new User("Thomas", "Des Ruisseaux", "lyesaid29@gmail.com", "12345","truisseaux"));
        liste.add(new User("Cedric", "Leao-Belzil", "lyesaid29@gmail.com", "12345","cbelzil"));

        listeFriends.add(new User("fabrice", "dehoule", "lyesaid29@gmail.com", "12345","cbelzil"));

        //Affichage par défaut
        adapterListe = new AdapterListe(liste);
        rv.setAdapter(adapterListe);
        //changement de selection
        changeSelectedRadio(view);

        return view;
    }

    private void changeSelectedRadio(View view) {
        // Écouteur pour la sélection
        RadioGroup radioGroup = view.findViewById(R.id.rg_selectBoard);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbFriends) {
                adapterListe = new AdapterListe(listeFriends);
            } else {
                adapterListe = new AdapterListe(liste);
            }
            rv.setAdapter(adapterListe);
        });
    }
}