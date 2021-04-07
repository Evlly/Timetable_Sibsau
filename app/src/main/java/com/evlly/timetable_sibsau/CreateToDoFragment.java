package com.evlly.timetable_sibsau;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.evlly.timetable_sibsau.model.Note;

import java.util.zip.Inflater;

public class CreateToDoFragment extends Fragment {

    private Note note;
    private EditText editText;
    Bundle bundle;

    public static final String EXTRA_NOTE = "CreateToDoFragment.EXTRA_NOTE";




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create_to_do, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).setTitle("Заметка");

        editText = view.findViewById(R.id.text_to_do);

        bundle = this.getArguments();
        if(bundle!= null){
            note = bundle.getParcelable(EXTRA_NOTE);
            editText.setText(note.text);
        }
        else{
            note = new Note();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.to_do_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();//мб изменить на транзакцию
                break;
            case R.id.action_save:
                if (editText.getText().length() > 0) {
                    note.text = editText.getText().toString();
                    note.done = false;
                    note.timestamp = System.currentTimeMillis();
                    if(bundle!=null){
                        App.getInstance().getNoteDao().update(note);
                    }
                    else{
                        App.getInstance().getNoteDao().insert(note);
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ToDoFragment()).commit();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
