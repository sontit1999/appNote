package com.example.notesmasteer.fragment.edit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.notesmasteer.R;
import com.example.notesmasteer.base.BaseFragment;
import com.example.notesmasteer.callback.CircleMenuCallback;
import com.example.notesmasteer.callback.NoteDao;
import com.example.notesmasteer.databinding.FragEditBinding;
import com.example.notesmasteer.fragment.bottomsheet.BackgroundBottomSheetFragment;
import com.example.notesmasteer.model.AppDatabase;
import com.example.notesmasteer.model.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditFragment extends BaseFragment<FragEditBinding,EditViewModel> {
    Note note;
    NoteDao noteDao;
    AppDatabase database;
    CircleMenuCallback menuCallback;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        menuCallback = (CircleMenuCallback) context;
        Bundle bundle = getArguments();
        if(bundle!=null && bundle.getSerializable("note")!= null){
            note = (Note) bundle.getSerializable("note");
        }else{
            note = new Note();
        }
    }

    @Override
    public Class<EditViewModel> getViewmodel() {
        return EditViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_edit;
    }

    @Override
    public void setBindingViewmodel() {
       binding.setViewmodel(viewmodel);
       initDatabase();
    }

    @Override
    public void ViewCreated() {
        binding.setNote(note);
        event();
    }

    private void event() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
                getController().navigateUp();
            }
        });
        binding.edtNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String content = binding.edtNote.getText().toString().trim();
                note.setContent(content);
            }
        });
        binding.edtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String title = binding.edtTitle.getText().toString().trim();
                note.setTitle(title);
            }
        });
        binding.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog("Bạn có chắc chắn muốn xóa ghi chú này chứ ?");
            }
        });
    }
    private void saveNote(){
        noteDao.update(note);
    }
    private void initDatabase() {
        // init room database
        database = Room.databaseBuilder(getContext(), AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        noteDao = database.getNoteDAO();
    }

    @Override
    public void onResume() {
        super.onResume();
        menuCallback.hiddenMenuCircle(true);
    }
    public void showDeleteDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton("Xóa lun ^^", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noteDao.delete(note);
                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                getController().navigateUp();
            }
        });
        builder.setNegativeButton("Không xóa nữa ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
