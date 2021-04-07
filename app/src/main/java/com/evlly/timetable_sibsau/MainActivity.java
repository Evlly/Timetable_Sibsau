package com.evlly.timetable_sibsau;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.evlly.timetable_sibsau.R;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Тестирование";
    public AutoCompleteTextView editText;
    SharedPreferences mySharedPreferences;
    LinearLayout layout;
    int sum_groups = 1777;
    final String[] groups = new String[sum_groups];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //getSupportActionBar().hide();
        editText=(AutoCompleteTextView) findViewById(R.id.editText);
        layout = (LinearLayout)findViewById(R.id.layout);
        mySharedPreferences = getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        if(mySharedPreferences.getString("GROUP","")!=""){
            Intent i = new Intent(MainActivity.this,TimetableActivity.class);
            startActivity(i);
        } else{
            layout.setVisibility(layout.VISIBLE);
            String message = getIntent().getStringExtra("MESSAGE");
            if (message!=null) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
            try {
                InputStream is = getAssets().open("groups.txt");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String all_groups = new String(buffer);
                int i=0;
               for (String subgroup : all_groups.split("\n")){
                    groups[i]=subgroup.substring(subgroup.indexOf(";")+1);
                    i++;
               }
                editText.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, groups));

            }
            catch (IOException e){
                e.printStackTrace();
            }

        }

    }



    public void ClickToTimetable(View v){
        String group = editText.getText().toString().toUpperCase();
        if (group.isEmpty()){
            Toast.makeText(getApplicationContext(), "Введите название своей группы", Toast.LENGTH_SHORT).show();
        }
         else if (group.contains(" ")){
            group = group.substring(0,group.indexOf(" "));
        }
        else{
            mySharedPreferences = getSharedPreferences("APP_PREFERENCES",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("GROUP",group);
            editor.apply();
            Intent i = new Intent(MainActivity.this,TimetableActivity.class);
            startActivity(i);
            finish();
        }
    }

}