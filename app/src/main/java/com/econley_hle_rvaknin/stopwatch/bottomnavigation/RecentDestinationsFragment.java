package com.econley_hle_rvaknin.stopwatch.bottomnavigation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.econley_hle_rvaknin.stopwatch.R;
import com.econley_hle_rvaknin.stopwatch.RecyclerViewAdapter;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * A fragment representing a list of Items.
 */
public class RecentDestinationsFragment extends Fragment {
    RecyclerView recyclerView;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RecyclerViewAdapter adapter;
    private LinkedList<String> recentDestinations;
    Activity activity;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecentDestinationsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecentDestinationsFragment newInstance(int columnCount) {
        RecentDestinationsFragment fragment = new RecentDestinationsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        recentDestinations = loadRecents();
        System.out.println(recentDestinations.size());
        activity = getActivity();
        System.out.println("activity = " + activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recent_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        recentDestinations = loadRecents();
        adapter = new RecyclerViewAdapter(recentDestinations);
        recyclerView.setAdapter(adapter);
    }

    // oh nooooooo
    // the copy pasta
    // it burns
    // this code really should've been in a reusable class where both your MapActivity and this Fragment could call it.
    private LinkedList<String> loadRecents() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPreferences.getString("recent destination list", null);
        Type type = new TypeToken<LinkedList<String>>() {}.getType();
        recentDestinations = gson.fromJson(json, type);

        if(recentDestinations == null){
            recentDestinations = new LinkedList<>();
        }

        // dedup list of recent destinations
        HashSet<String> set = new HashSet<>(recentDestinations);

        return new LinkedList<>(set);
    }
}