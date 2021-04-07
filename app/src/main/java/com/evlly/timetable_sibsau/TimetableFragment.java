package com.evlly.timetable_sibsau;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.Calendar;

public class TimetableFragment extends Fragment {
    int todayWeek;
    int todayDay;
    int today=-1;
    Calendar calendar;

    public int setToday(){
        todayWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        todayDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if(todayWeek%2==0){
            todayWeek=0;
        }
        else {
            todayWeek=1;
        }
        if(todayDay==1){
            todayDay=6;
        }
        else{
            todayDay = todayDay-2;
        }
        calendar = Calendar.getInstance();
        today = todayWeek*7+todayDay;
        return today;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_timetable, container, false);
        PagerAdapter pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), getActivity());
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(setToday());
        //pagerAdapter.get


        return view;
    }
}
