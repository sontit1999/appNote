package com.example.notesmasteer.fragment.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.example.notesmasteer.R;
import com.example.notesmasteer.base.BaseFragment;
import com.example.notesmasteer.callback.CircleMenuCallback;
import com.example.notesmasteer.callback.NoteCallBack;
import com.example.notesmasteer.callback.NoteDao;
import com.example.notesmasteer.databinding.FragHomeBinding;
import com.example.notesmasteer.model.AppDatabase;
import com.example.notesmasteer.model.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends BaseFragment<FragHomeBinding,HomeViewModel> {
    ArrayList<Note> listNote;
    NoteDao noteDao;
    AppDatabase database;
    private static final int TIME_DELAY = 3000;
    CircleMenuCallback menuCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        menuCallback = (CircleMenuCallback) context;
    }

    @Override
    public Class<HomeViewModel> getViewmodel() {
        return HomeViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_home;
    }

    @Override
    public void setBindingViewmodel() {
         binding.setViewmodel(viewmodel);
         initDatabase();
         initRecyclerviewNote();
    }

    private void initRecyclerviewNote() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        binding.rvNotes.setLayoutManager(staggeredGridLayoutManager);
        binding.rvNotes.setAdapter(viewmodel.noteAdapter);
    }

    private void initDatabase() {
        // init room database
        database = Room.databaseBuilder(getContext(), AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        noteDao = database.getNoteDAO();
      //  addSampleNoteToDatabase();
    }

    @Override
    public void ViewCreated() {
        event();
        // quan sát listnote trong viewmodel
        viewmodel.getListNote().observe(this, new Observer<ArrayList<Note>>() {
            @Override
            public void onChanged(ArrayList<Note> notes) {
                viewmodel.noteAdapter.setList(notes);
                viewmodel.noteAdapter.setCallBack(new NoteCallBack() {
                    @Override
                    public void onNoteClick(Note note) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("note",note);
                        getController().navigate(R.id.action_HomeFragment_to_EditFragment,bundle);
                    }

                    @Override
                    public void onCheckBoxCheck(final View view, final Note note) {
                        final CheckBox cb = (CheckBox) (view);
                        if(cb.isChecked()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Bạn có chắc đánh dấu là ghi chú này đã hoàn thành ?");
                            builder.setPositiveButton("Chắc ^^", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    view.setVisibility(View.GONE);
                                    // Toast.makeText(getActivity(), "Checked", Toast.LENGTH_SHORT).show();
                                    note.setBackgroundColor(1);
                                    noteDao.update(note);
                                    getNote();
                                }
                            });
                            builder.setNegativeButton(" Suy nghĩ lại  ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    cb.setChecked(false);
                                }
                            });

                            builder.create().show();
                        }

                    }
                });
            }
        });
        getNote();
    }

    private void getNote() {
        listNote = (ArrayList<Note>) noteDao.getItems();
        Collections.reverse(listNote);
        viewmodel.setListNote(listNote);
    }

    private void event() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchNote(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchNote(newText);
                return false;
            }
        });
        binding.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                menuCallback.hiddenMenuCircle(false);
                return false;
            }
        });
        binding.searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuCallback.hiddenMenuCircle(true);
            }
        });
        // lắng nghe searchview focus or not
        binding.searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    menuCallback.hiddenMenuCircle(true);
                }else{
                    menuCallback.hiddenMenuCircle(false);
                }
            }
        });
    }

    private void searchNote(String query) {
        ArrayList<Note> arrResult = new ArrayList<>();
        for(Note i : listNote){
            if(i.getContent().contains(query)){
                arrResult.add(i);
            }
        }
        viewmodel.setListNote(arrResult);
    }
    @Override
    public void onResume() {
        super.onResume();
        menuCallback.hiddenMenuCircle(false);
    }
}
