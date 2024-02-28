package com.example.cumpinion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.cumpinion.ui.main.MainActivity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import classes.AdapterStreak;
import classes.Streak;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //MES AFFAIRES A MOI LA (THOMAS)
    CalendarView cView;

    RecyclerView rc;

    AdapterStreak adapS;

    List<Streak> liste = new ArrayList<>();


    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_history, container, false);
        cView = view.findViewById(R.id.vCalen);
        rc = view.findViewById(R.id.rvView);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(
                new LinearLayoutManager(getContext()));
        Calendar calendar = Calendar.getInstance();

        java.sql.Date sqlDateDeb = new java.sql.Date(System.currentTimeMillis());
        java.sql.Date sqlDateFin = new java.sql.Date(System.currentTimeMillis());
        Streak S = new Streak(sqlDateDeb,sqlDateFin, 3, 3);
        liste.add(S);
        adapS = new AdapterStreak(liste);


        rc.setAdapter(adapS);

        calendar.add(Calendar.DATE, 1);

        calendar.set(2023, 12,12);

        return view;
    }

    private void highlightDate(int year, int month, int dayOfMonth, CalendarView cv) {
        long selectedDateMillis = getDateInMillis(year, month, dayOfMonth);
        cv.setDate(selectedDateMillis); // Set the selected date again to ensure it's highlighted
    }

    private long getDateInMillis(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTimeInMillis();
    }

}