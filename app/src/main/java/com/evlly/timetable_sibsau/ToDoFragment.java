package com.evlly.timetable_sibsau;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evlly.timetable_sibsau.model.Note;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.evlly.timetable_sibsau.CreateToDoFragment.EXTRA_NOTE;

public class ToDoFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ToDoListAdapter.RecyclerViewClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_to_do, container, false);
        fab = (FloatingActionButton)view.findViewById(R.id.fab);

        recyclerView = (RecyclerView)view.findViewById(R.id.list_to_do);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        final ToDoListAdapter adapter = new ToDoListAdapter(listener);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateToDoFragment()).commit();

            }
        });

        ToDoViewModel viewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        viewModel.getNoteLiveData().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setItems(notes);
            }
        });

        return view;
    }

    public void setOnClickListener(){
        listener = new ToDoListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position, Note note) {
                CreateToDoFragment fragment = new CreateToDoFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_NOTE, note);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        };
    }

}
