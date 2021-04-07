package com.evlly.timetable_sibsau;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class TimetableActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "Тестирование";
    private SwipeRefreshLayout swipe;
    public boolean progress = true;
    public ImageButton exit;
    public TextView number_week;
    public TextView name;
    public TextView date_text;
    public ImageButton btn_back;
    public ImageButton btn_next;
    public ImageButton btn_today;
    public ArrayList<String> disciplines = new ArrayList<String>();
    public ArrayList<String> time = new ArrayList<String>();
    public ArrayList<String> days = new ArrayList<String>();
    public ArrayList<String> links = new ArrayList<String>();
    public ArrayList<String> time_for_exams = new ArrayList<String>();
    private ProgressBar progressb;
    boolean error = false;
    public ArrayList<Day> days_arr = new ArrayList<Day>();
    public ArrayList<Day> exams_arr = new ArrayList<Day>();
    private ListView listView;
    SharedPreferences mySharedPreferences;
     MyTask mt = new MyTask();
    SimpleAdapter adapter;
    int current=0;
    int today=-1;
    public String[] week = {"Понедельник","Вторник","Среда","Четверг","Пятница","Суббота","Воскресенье"};
    DateFormat dateFormat = new SimpleDateFormat("dd.MM");
    Calendar calendar;
    Date date;
    boolean first_time = false;
    int todayWeek;
    int todayDay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        exit = (ImageButton)findViewById(R.id.imageButton);
        number_week = (TextView)findViewById(R.id.numberWeek);
        name = (TextView)findViewById(R.id.name);
        date_text = (TextView)findViewById(R.id.date);
        swipe = (SwipeRefreshLayout)findViewById(R.id.swipe);
        btn_back = (ImageButton)findViewById(R.id.button_back);
        btn_next = (ImageButton)findViewById(R.id.button_next);
        btn_today = (ImageButton)findViewById(R.id.button_today);
        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.GRAY);
        progressb = (ProgressBar)findViewById(R.id.progressBar);
        listView = (ListView)findViewById(R.id.list);
        registerForContextMenu(listView);
        progressb.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        //getSupportActionBar().hide();
        loadData();
        if(first_time){
            mt.execute();
        }
        else{
            setToday();
            Intent i = new Intent(TimetableActivity.this,NewTimetableActivity.class);
            startActivity(i);
        }
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listView.getChildAt(0) != null) {
                    swipe.setEnabled(listView.getFirstVisiblePosition() == 0 && listView.getChildAt(0).getTop() == 0);
                }
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.link:
                goToTeacher(info.position);
                return true;
            default:
            return super.onContextItemSelected(item);
        }
    }

    public void goToTeacher(int position) {
        Intent browseIntent;
        for(String link :days_arr.get(current).lessons.get(position).link_to_teacher.split(" ")) {
            if (!link.equals("")) {
                browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://timetable.pallada.sibsau.ru" + link));
                startActivity(browseIntent);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void parsSite(ArrayList<String> list, String query, Document doc){
         Elements title;
         title = doc.select(query);
         list.clear();
        for (Element titles: title){
            list.add(titles.text());
        }
    }

    public void getLinks(Document doc){
        Element mBody = doc.body();
        Elements links_all = mBody.getElementsByTag("a");
        links.clear();
        for(Element link: links_all){
            String s = link.attr("href");
            if (s.contains("professor")) {
                links.add(s);
            }
        }
    }

    public void exit(){
        mySharedPreferences = getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("GROUP","");
        editor.putString("LESSONS",null);
        editor.apply();
        Intent i_exit = new Intent(TimetableActivity.this,MainActivity.class);
        startActivity(i_exit);
        finishAffinity();
    }

    public void exit(String message){
        mySharedPreferences = getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("GROUP","");
        editor.putString("LESSONS",null);
        editor.apply();
        Intent i_exit = new Intent(TimetableActivity.this,MainActivity.class);
        i_exit.putExtra("MESSAGE",message);
        startActivity(i_exit);
        finishAffinity();
    }

    public int numLessons(ArrayList<String> pairs, int start){
        int k=0;
        for(int i=start;i<pairs.size();i++){
            if(pairs.get(i).compareTo("Дисциплина")==0){
                pairs.remove(i);
                break;
            }
            else
                k++;
        }
        return k;
    }


    public void setLessons(int current){
        ArrayList<HashMap<String,Object>> data = new ArrayList<>(days_arr.get(current).sum_lessons);
        HashMap<String, Object> map;
        for (int i=0; i<days_arr.get(current).sum_lessons; i++){
            map = new HashMap<>();
            map.put("NAME",days_arr.get(current).lessons.get(i).name);
            map.put("TIME",days_arr.get(current).lessons.get(i).time);
            map.put("PLACE",days_arr.get(current).lessons.get(i).place);
            map.put("TEACHER",days_arr.get(current).lessons.get(i).teacher);
            map.put("TYPE",days_arr.get(current).lessons.get(i).type);
            data.add(map);
        }
        String[] from = {"NAME","TIME","PLACE","TEACHER","TYPE"};
        int[] to = {R.id.adapter_name,R.id.adapter_time,R.id.adapter_place,R.id.adapter_teacher,R.id.adapter_type};
        adapter = new SimpleAdapter(this, data, R.layout.adapter, from, to);
        listView.setAdapter(adapter);
        date = calendar.getTime();
        String s_date = dateFormat.format(date);
        date_text.setText(s_date);
        name.setText(days_arr.get(current).name);
        number_week.setText(String.valueOf(days_arr.get(current).number_week)+" неделя");
    }

    @Override
    public void onRefresh() {
        swipe.setRefreshing(true);
        mt = new MyTask();
        mt.execute();
        swipe.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(false);
            }
        },2000);
    }

    public void saveData(){
        mySharedPreferences = getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(days_arr);
        editor.putString("LESSONS",json);
        json = gson.toJson(exams_arr);
        editor.putString("EXAMS", json);
        editor.apply();
    }

    public void loadData(){
        mySharedPreferences = getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        Gson gson = new Gson();
        String json = mySharedPreferences.getString("LESSONS",null);
        Type type = new TypeToken<ArrayList<Day>>() {}.getType();
        days_arr = gson.fromJson(json, type);
        if (days_arr==null){
            first_time = true;
        }
    }

    public void setToday(){
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
        current=today;
        setLessons(current);
    }



    public void ExitMe(View v){
        exit();
    }
    public void ClickNext(View v){
        if(current==days_arr.size()-1){
            current=0;
        }
        else {
            current++;
        }
        calendar.add(Calendar.DATE,1);
        setLessons(current);
    }
    public void ClickBack(View v){
        if(current==0){
            current=days_arr.size()-1;
        }
        else {
            current--;
        }
        calendar.add(Calendar.DATE,-1);
        setLessons(current);
    }
    public void ClickToday(View v){
        setToday();
    }

    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                if (progress && first_time) {
                    progressb.setVisibility(progressb.VISIBLE);
                }
                if(first_time) {
                    exit.setVisibility(exit.INVISIBLE);
                    btn_back.setVisibility(btn_back.INVISIBLE);
                    btn_next.setVisibility(btn_next.INVISIBLE);
                    btn_today.setVisibility(btn_today.INVISIBLE);
                }

        }

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