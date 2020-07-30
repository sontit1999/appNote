package com.example.notesmasteer.fragment.home;

import androidx.lifecycle.MutableLiveData;

import com.example.notesmasteer.adapter.Note2Adapter;
import com.example.notesmasteer.adapter.NoteAdapter;
import com.example.notesmasteer.base.BaseViewmodel;
import com.example.notesmasteer.callback.NoteDao;
import com.example.notesmasteer.model.AppDatabase;
import com.example.notesmasteer.model.Note;

import java.util.ArrayList;

public class HomeViewModel extends BaseViewmodel {
    NoteAdapter noteAdapter = new NoteAdapter();
    MutableLiveData<ArrayList<Note>> listNote = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Note>> getListNote(){
        return listNote;
    }
    public void setListNote(ArrayList<Note> arrayList){
        listNote.postValue(arrayList);
    }
}
