package com.evlly.timetable_sibsau;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExamsListAdapter extends RecyclerView.Adapter<ExamsListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Day> exams;

    ExamsListAdapter(Context context, ArrayList<Day> exams){
        this.inflater = LayoutInflater.from(context);
        this.exams = exams;
    }


    @Override
    public ExamsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter, parent, false);
        return new ViewHolder(view);
    }

    //потом переделать по геттеры и сеттеры
    @Override
    public void onBindViewHolder(ExamsListAdapter.ViewHolder viewHolder, int position) {
        Day day = exams.get(position);
        Lesson exam = day.lessons.get(0);
        viewHolder.timeView.setText(day.name +"  "+ exam.time);
        viewHolder.nameView.setText(exam.name);
        viewHolder.typeView.setText(exam.type);
        viewHolder.teacherView.setText(exam.teacher);
        viewHolder.placeView.setText(exam.place);

    }

    @Override
    public int getItemCount() {
        return exams.size();
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
