package com.evlly.timetable_sibsau;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PagerFragment extends Fragment {

    static final String DAY = "day";
    static final String WEEK_DAY = "weekDay";
    static final String WEEK = "week";
    ArrayList<Lesson> lessonsList;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager, container, false);
        Bundle arguments = getArguments();

        if (arguments != null){
            String daysJson = arguments.getString(DAY);
            String dayOfWeek = arguments.getString(WEEK_DAY);
            String numberWeek = arguments.getString(WEEK);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Lesson>>() {}.getType();
            lessonsList = gson.fromJson(daysJson, type);
            displayValues(view, lessonsList, numberWeek, dayOfWeek);
        }

        return view;
    }

    private void displayValues(View v, ArrayList<Lesson> lessons, String week, String day) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_timetable, null);
        RecyclerView recyclerView = v.findViewById(R.id.recycler);
        TextView textWeek = v.findViewById(R.id.textWeek);
        TextView textDay = v.findViewById(R.id.textDay);
        textWeek.setText(week);
        textDay.setText(day);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        if (lessons != null) {
            ListAdapter adapter = new ListAdapter(getActivity(), lessons);
            recyclerView.setAdapter(adapter);
        }
    }
}
