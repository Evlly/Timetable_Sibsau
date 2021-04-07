package com.evlly.timetable_sibsau;

public class Lesson {
    String name="";
    String time;
    String place="";
    String teacher="";
    String type="";
    String link_to_teacher=" ";


    public void pars(String s){
        type = s.substring(s.indexOf("("), s.indexOf(")") + 1);
        name += s.substring(0, s.indexOf("("));
        teacher = s.substring(s.indexOf(")") + 2, s.indexOf("корп.") - 1);
        place = s.substring(s.indexOf("корп."));
    }
}
