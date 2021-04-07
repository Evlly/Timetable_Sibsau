package com.evlly.timetable_sibsau;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<String> places_s;
    private RecyclerViewClickListener listener;

    PlacesListAdapter(Context context, ArrayList<String> places_s, RecyclerViewClickListener listener){
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.places_s = places_s;
    }


    @Override
    public PlacesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.places_adapter, parent, false);
        return new ViewHolder(view);
    }

    //потом переделать по геттеры и сеттеры
    @Override
    public void onBindViewHolder(PlacesListAdapter.ViewHolder viewHolder, int position) {
        String place = places_s.get(position);
        viewHolder.place.setText(place);

    }

    @Override
    public int getItemCount() {
        return places_s.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView place;

        ViewHolder(View view){
            super(view);
            place = (TextView)view.findViewById(R.id.places_adapter_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
