package com.evlly.timetable_sibsau;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.evlly.timetable_sibsau.model.Note;

import java.util.ArrayList;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

    private SortedList<Note> sortedList;
    private ToDoListAdapter.RecyclerViewClickListener listener;


    public ToDoListAdapter(ToDoListAdapter.RecyclerViewClickListener listener){
        this.listener = listener;
        sortedList = new SortedList<>(Note.class, new SortedList.Callback<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                if (!o2.done && o1.done) {
                    return 1;
                }
                if (o2.done && !o1.done) {
                    return -1;
                }
                return (int) (o2.timestamp - o1.timestamp);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Note oldItem, Note newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Note item1, Note item2) {
                return item1.uid == item2.uid;
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    @NonNull
    @Override
    public ToDoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_to_do, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(sortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public void setItems(List<Note> notes) {
        sortedList.replaceAll(notes);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView noteText;
        CheckBox completed;
        View delete;
        Note note;
        boolean silentUpdate;
        public static final String EXTRA_NOTE = "CreateToDoFragment.EXTRA_NOTE";


        ViewHolder(View view){
            super(view);
            noteText = view.findViewById(R.id.note_text);
            completed = view.findViewById(R.id.completed);
            delete = view.findViewById(R.id.delete);
            //view.setOnClickListener(this);

            /*view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateToDoFragment fragment = new CreateToDoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_NOTE, note);
                    fragment.setArguments(bundle);
                    //додумать переход на фрагмент редактирования
                }
            });*/


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    App.getInstance().getNoteDao().delete(note);
                }
            });

            completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (!silentUpdate) {
                        note.done = checked;
                        App.getInstance().getNoteDao().update(note);
                    }
                    updateStrokeOut();
                }
            });

        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition(), note);
        }




        public void bind(Note note) {
            this.note = note;

            noteText.setText(note.text);
            updateStrokeOut();

            silentUpdate = true;
            completed.setChecked(note.done);
            silentUpdate = false;
        }

        private void updateStrokeOut() {
            if (note.done) {
                noteText.setPaintFlags(noteText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                noteText.setPaintFlags(noteText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }


    }
    public interface RecyclerViewClickListener{
        void onClick(View v, int position, Note note);
    }


}
