package com.example.notesmasteer.callback;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notesmasteer.model.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    public void insert(Note... notes);
    @Update
    public void update(Note... notes);
    @Delete
    public void delete(Note note);
    @Query("SELECT * FROM notes")
    public List<Note> getItems();
    @Query("DELETE FROM notes")
    public void deleteAllNote();
    @Query("SELECT * FROM notes WHERE id = :id")
    public Note getItemById(Long id);
}
