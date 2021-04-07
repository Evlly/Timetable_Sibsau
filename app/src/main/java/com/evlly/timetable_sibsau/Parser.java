package com.evlly.timetable_sibsau;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Parser extends AsyncTask {

    SharedPreferences mySharedPreferences;

    @Override
    protected Void doInBackground(Void... voids) {
        Document doc = null;
        mySharedPreferences = getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        String group = mySharedPreferences.getString("GROUP","");
        try {
            InputStream is = getAssets().open("groups.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String all_groups = new String(buffer);
            group = ";"+group;
            String c=all_groups.substring(all_groups.indexOf(group),all_groups.indexOf(group)+1);
            int k=0;
            while (!c.contains("\n")){
                k++;
                c=all_groups.substring(all_groups.indexOf(group)-k,all_groups.indexOf(group)+1-k);
            }
            group = all_groups.substring(all_groups.indexOf(group)-k+1,all_groups.indexOf(group));
            doc = Jsoup.connect("https://timetable.pallada.sibsau.ru/timetable/group/"+group).get();
            if (!first_time) {
                days_arr.clear();
            }
            else{
                days_arr = new ArrayList<>();

            }
            parsSite(disciplines,".discipline",doc);
            parsSite(time,".hidden-xs",doc);
            parsSite(days,".name.text-center",doc);
            parsSite(time_for_exams,".time.text-center", doc);
            getLinks(doc);
            if (time.size()<=1){
                if (first_time) {
                    exit("Расписание недоступно");
                }
                else {
                    showToast("Невозможно обновить");
                }
                mt.cancel(true);
            }
            first_time=false;

        }
        catch (IOException e){
            e.printStackTrace();
            if (first_time) {
                exit("Расписание недоступно");
            }
            else {
                showToast("Невозможно обновить");
            }
            mt.cancel(true);
        }
        catch (RuntimeException e){
            mt.cancel(true);
            exit("Некорректный ввод");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressb.setVisibility(progressb.INVISIBLE);
        progress = false;
        exit.setVisibility(exit.VISIBLE);
        btn_back.setVisibility(btn_back.VISIBLE);
        btn_next.setVisibility(btn_next.VISIBLE);
        btn_today.setVisibility(btn_today.VISIBLE);
        time.remove(0);
        time.remove(time.size()-1);
        disciplines.remove(0);
        int exams = 0;
        for (String disp: disciplines){
            if (disp.contains("Экзамен")){
                exams++;
            }
        }
        if (exams>0){
            for (int i=0;i < exams; i++){
                Day day = new Day();
                day.lessons = new ArrayList<Lesson>();
                Lesson lesson = new Lesson();
                day.name = days.get(days.size()-i-1);
                lesson.time = time_for_exams.get(time_for_exams.size()-i*2-1);
                lesson.pars(disciplines.get(disciplines.size()-i*2-1));
                day.lessons.add(lesson);
                exams_arr.add(0,day);
            }
        }
        for(int i=0; i<exams*2-1; i++){
            disciplines.remove(disciplines.size()-1);
        }
        for(int i=0; i<exams; i++){
            days.remove(days.size()-1);
        }
        int sc_time_lesson=0;
        for(int i=0; i<days.size();i++){
            Day day = new Day();
            day.lessons = new ArrayList<Lesson>();
            day.name = days.get(i);
            if (day.name.contains("сегодня")){
                day.name = day.name.substring(0,day.name.indexOf("сегодня"));
            }
            day.sum_lessons = numLessons(disciplines,sc_time_lesson);
            for (int j=0;j<day.sum_lessons;j++){
                String s = disciplines.get(sc_time_lesson);
                Lesson lesson = new Lesson();
                if (s.contains("подгруппа") && s.indexOf("подгруппа")<s.indexOf("каб.")){
                    lesson.time = time.get(sc_time_lesson);
                    ArrayList<Integer> indexes = new ArrayList<>();
                    int index=0;
                    while (s.indexOf("подгруппа", index+1)!=-1){
                        index = s.indexOf("подгруппа", index+1);
                        indexes.add(index);
                    }
                    String subgroup, type, name, teacher,place, link;
                    for ( index=0;index<indexes.size();index++){
                        if (index==indexes.size()-1){
                            subgroup = s.substring(indexes.get(index)-2);
                        } else {
                            subgroup = s.substring(indexes.get(index) - 2, indexes.get(index + 1) - 2);
                        }
                        type = (subgroup.substring(subgroup.indexOf("("), subgroup.indexOf(")") + 1)) + "; ";
                        name = (subgroup.substring(0, subgroup.indexOf("("))) + "; ";
                        teacher = (subgroup.substring(subgroup.indexOf(")") + 2, subgroup.indexOf("корп.") - 1)) + "; ";
                        place = (subgroup.substring(subgroup.indexOf("корп."))) + "; ";
                        // link = (subgroup.substring(subgroup.indexOf("("), subgroup.indexOf(")") + 1)) + "; ";
                        if (!lesson.type.equals(type)) lesson.type += type;
                        if (!lesson.name.equals(name)) lesson.name += name;
                        if (!lesson.teacher.equals(teacher)) lesson.teacher += teacher;
                        if (index==indexes.size()-1){
                            place = place.substring(0, place.indexOf(";"))+" ; ";
                        }
                        if (!lesson.place.equals(place)) lesson.place += place;
                        lesson.link_to_teacher += links.get(0) + " ";
                        links.remove(0);

                    }
                    day.lessons.add((lesson));
                    sc_time_lesson++;
                }
                else {
                    if(s.contains("подгруппа")){
                        lesson.name+=s.substring(s.length()-11)+" ";
                        s=s.substring(0,s.length()-11);
                    }
                    lesson.time = time.get(sc_time_lesson);
                    lesson.link_to_teacher = links.get(0);
                    lesson.pars(s);
                    links.remove(0);
                    day.lessons.add(lesson);
                    sc_time_lesson++;
                }
            }
            days_arr.add(day);
        }

        while (days_arr.size()!=14){
            Day day = new Day();
            days_arr.add(day);
        }
        int k=0;
        for (int i=0; i<2; i++){
            for (int j=0;j<7;j++, k++){
                if (!days_arr.get(k).name.contains(week[j])){
                    Day day = new Day();
                    day.number_week=i+1;
                    day.name = week[j] + "- Выходной";
                    days_arr.add(k,day);
                }
                else{
                    days_arr.get(k).number_week=i+1;
                }
            }
        }
        while (days_arr.size()!=14){
            days_arr.remove(days_arr.size()-1);
        }
        setToday();
        saveData();
        disciplines.clear();
        time.clear();
        links.clear();
        days.clear();
        Intent i = new Intent(TimetableActivity.this,NewTimetableActivity.class);
        startActivity(i);
    }
}
}
