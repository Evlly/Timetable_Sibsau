package com.evlly.timetable_sibsau;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ExamsFragment extends Fragment {

    SharedPreferences mySharedPreferences;
    ArrayList<Day> exams;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exams, container, false);
        recyclerView = view.findViewById(R.id.recycler_exams);
        mySharedPreferences = getActivity().getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        Gson gson = new Gson();
        String json = mySharedPreferences.getString("EXAMS",null);
        Type type = new TypeToken<ArrayList<Day>>() {}.getType();
        exams = gson.fromJson(json, type);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        ExamsListAdapter adapter = new ExamsListAdapter(getActivity(), exams);
        recyclerView.setAdapter(adapter);


        return view;
    }
}
