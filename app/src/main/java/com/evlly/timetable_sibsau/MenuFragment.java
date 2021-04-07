package com.evlly.timetable_sibsau;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class MenuFragment extends Fragment {

    private ImageButton mapButton;
    private ImageButton toDoButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);

        mapButton = (ImageButton)view.findViewById(R.id.imageButtonMap);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlacesFragment()).commit();
            }
        });

        toDoButton = (ImageButton)view.findViewById(R.id.imageButtonToDo);
        toDoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ToDoFragment()).commit();
            }
        });




        return view;
    }


}
