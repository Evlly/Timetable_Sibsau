package com.evlly.timetable_sibsau;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Day> days;
    private ArrayList<String> days_s;
    private SharedPreferences mySharedPreferences;





    public PagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        mySharedPreferences = context.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        Gson gson = new Gson();
        String json = mySharedPreferences.getString("LESSONS",null);
        Type type = new TypeToken<ArrayList<Day>>() {}.getType();
        days = gson.fromJson(json, type);
        days_s = new ArrayList<>();


        for(Day day: days){
            if(day.sum_lessons!=0) {
                gson = new Gson();
                json = gson.toJson(day.lessons);
                days_s.add(json);
            }
            else days_s.add("");
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();


        arguments.putString(PagerFragment.DAY, days_s.get(position));
        arguments.putString(PagerFragment.WEEK_DAY, days.get(position).name);
        arguments.putString(PagerFragment.WEEK, days.get(position).number_week+" неделя");

        PagerFragment pagerFragment = new PagerFragment();
        pagerFragment.setArguments(arguments);

        return pagerFragment;
    }

    @Override
    public int getCount() {
        return days_s.size();
    }

}
