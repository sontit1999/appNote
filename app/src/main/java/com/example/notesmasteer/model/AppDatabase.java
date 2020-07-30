package com.example.notesmasteer.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.notesmasteer.callback.NoteDao;

@Database(entities = {Note.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao getNoteDAO();
}
