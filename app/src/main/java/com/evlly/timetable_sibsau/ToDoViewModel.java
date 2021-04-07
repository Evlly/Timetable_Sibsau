package com.evlly.timetable_sibsau;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.evlly.timetable_sibsau.model.Note;

import java.util.List;

public class ToDoViewModel extends ViewModel {

    private LiveData<List<Note>> noteLiveData = App.getInstance().getNoteDao().getAllLiveData();

    public LiveData<List<Note>> getNoteLiveData() {
        return noteLiveData;
    }
}
