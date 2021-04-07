package com.evlly.timetable_sibsau;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PlacesFragment extends Fragment {

    private HashMap<String, LatLng> places;
    private PlacesListAdapter.RecyclerViewClickListener listener;
    private ArrayList<String> places_s;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        places = new HashMap<>();
        places.put("Корпус \"А\"",new LatLng(56.011794, 92.973366));
        places.put("Корпус \"Б\"",new LatLng(56.015696, 92.986768));
        places.put("Корпус \"В\"",new LatLng(56.015696, 92.986768));
        places.put("Корпус \"Г\"",new LatLng(55.989315, 92.880978));
        places.put("Корпус \"Д\"",new LatLng(56.014424, 92.975134));
        places.put("Корпус \"Е\"",new LatLng(56.015638, 92.979983));
        places.put("Корпус \"К\"",new LatLng(56.011659, 92.974400));
        places.put("Корпус \"Л\"",new LatLng(56.012610, 92.974286));
        places.put("Корпус \"М\"",new LatLng(55.985892, 93.005720));
        places.put("Корпус \"Н\"",new LatLng(56.012305, 92.974499));
        places.put("Корпус \"О\"",new LatLng(55.983188, 92.897761));
        places.put("Корпус \"П\"",new LatLng(56.012002, 92.974361));
        places.put("Корпус \"Р\"",new LatLng(56.042793, 93.028117));
        places.put("Корпус \"С\"",new LatLng(55.999162, 92.950004));
        places.put("Корпус \"Ю\"",new LatLng(56.014861, 92.972182));
        places.put("Корпус \"Гл\"",new LatLng(56.012292, 92.869046));
        places.put("Корпус \"Цл\"",new LatLng(56.012292, 92.869046));
        places.put("Корпус \"СК\"",new LatLng(56.012292, 92.869046));
        places.put("Корпус \"УСК\"",new LatLng(56.012215, 92.871715));
        places.put("Корпус \"7\"",new LatLng(56.012292, 92.869046));
        places.put("Корпус \"8\"",new LatLng(56.012292, 92.869046));
        places.put("Корпус \"Ал\"",new LatLng(56.014679, 92.868133));
        places.put("Корпус \"Бл\"",new LatLng(56.014679, 92.868133));
        places.put("Корпус \"Вл\"",new LatLng(56.014679, 92.868133));
        places.put("Корпус \"Сл\"",new LatLng(56.005532, 92.843344));
        places.put("Аэрокосмический колледж",new LatLng(56.011659, 92.974400));
        places.put("Аэрокосмическая школа",new LatLng(56.018346, 92.977252));
        places.put("Спортзал №1",new LatLng(56.017409, 92.980903));
        places.put("Дворец водного спорта",new LatLng(56.019295, 92.981309));
        places.put("Стадион",new LatLng(56.030498, 93.020628));
        places.put("Бассейн \"Технологический\"",new LatLng(56.015366, 92.870288));
        places.put("Лыжная база",new LatLng(55.999706, 92.784568));
        places.put("Тренажерный за в корпусе \"Л\"",new LatLng(56.012610, 92.974286));
        places_s = new ArrayList<>();
        for (HashMap.Entry<String, LatLng> entry : places.entrySet()) {
            places_s.add(entry.getKey());
        }
        Collections.sort(places_s);
        displayValues(view);
        return view;
    }

    private void displayValues(View v) {
        setOnClickListener();
        RecyclerView recyclerView = v.findViewById(R.id.recycler_places);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        PlacesListAdapter adapter = new PlacesListAdapter(getActivity(), places_s, listener);
        recyclerView.setAdapter(adapter);
    }

    public void setOnClickListener(){
        listener = new PlacesListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment(places_s.get(position), places.get(places_s.get(position)))).commit();
            }
        };
    }


}
