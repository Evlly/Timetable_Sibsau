package com.evlly.timetable_sibsau;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Lesson> lessons;

    ListAdapter(Context context, ArrayList<Lesson> lessons){
        this.inflater = LayoutInflater.from(context);
        this.lessons = lessons;
    }


    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter, parent, false);
        return new ViewHolder(view);
    }

    //потом переделать по геттеры и сеттеры
    @Override
    public void onBindViewHolder( ListAdapter.ViewHolder viewHolder, int position) {
        Lesson lesson = lessons.get(position);
        viewHolder.timeView.setText(lesson.time);
        viewHolder.nameView.setText(lesson.name);
        viewHolder.typeView.setText(lesson.type);
        viewHolder.teacherView.setText(lesson.teacher);
        viewHolder.placeView.setText(lesson.place);

    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView timeView, nameView, typeView, teacherView, placeView;

        ViewHolder(View view){
            super(view);
            timeView = (TextView)view.findViewById(R.id.adapter_time);
            nameView = (TextView)view.findViewById(R.id.adapter_name);
            typeView = (TextView)view.findViewById(R.id.adapter_type);
            teacherView = (TextView)view.findViewById(R.id.adapter_teacher);
            placeView = (TextView)view.findViewById(R.id.adapter_place);
        }
    }
}
