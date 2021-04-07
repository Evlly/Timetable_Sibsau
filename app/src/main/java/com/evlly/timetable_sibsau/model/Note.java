package com.evlly.timetable_sibsau.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Note implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    public Note() {
    }

    @Override
    public int hashCode() {
        int result = uid;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (done ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Note note = (Note) obj;

        if (uid != note.uid) return false;
        if (timestamp != note.timestamp) return false;
        if (done != note.done) return false;
        return text != null ? text.equals(note.text) : note.text == null;
    }

    @ColumnInfo(name = "done")
    public boolean done;

    protected Note(Parcel in) {
        uid = in.readInt();
        text = in.readString();
        timestamp = in.readLong();
        done = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(text);
        dest.writeLong(timestamp);
        dest.writeByte((byte) (done ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
