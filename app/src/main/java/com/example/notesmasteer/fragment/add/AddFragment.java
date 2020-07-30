package com.example.notesmasteer.fragment.add;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.notesmasteer.R;
import com.example.notesmasteer.base.BaseFragment;
import com.example.notesmasteer.callback.CircleMenuCallback;
import com.example.notesmasteer.callback.NoteDao;
import com.example.notesmasteer.databinding.FragAddBinding;
import com.example.notesmasteer.model.AppDatabase;
import com.example.notesmasteer.model.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddFragment extends BaseFragment<FragAddBinding,AddViewModel> {
    CircleMenuCallback menuCallback;
    NoteDao noteDao;
    AppDatabase database;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        menuCallback = (CircleMenuCallback) context;
    }

    @Override
    public Class<AddViewModel> getViewmodel() {
        return AddViewModel.class;
    }

    @Override
    public int getLayoutID() {
        return R.layout.frag_add;
    }

    @Override
    public void setBindingViewmodel() {
        initDatabase();
        binding.setViewmodel(viewmodel);
    }

    @Override
    public void ViewCreated() {
          event();
    }

    private void event() {
        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveDialog("Bạn có muốn lưu ghi chú này không ? ^.^ ");
            }
        });
    }
    public void showSaveDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton("Lưu lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tittle = binding.edtTitle.getText().toString().trim();
                String content = binding.edtNote.getText().toString().trim();
                if(TextUtils.isEmpty(tittle)){
                    Toast.makeText(getActivity(), "Title must no empty !", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(content)){
                    Toast.makeText(getActivity(), "Content must no empty !", Toast.LENGTH_SHORT).show();
                }else{
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
                    Calendar calendar = Calendar.getInstance();
                    String timeCreate = simpleDateFormat.format(new Date());
                    Toast.makeText(getActivity(), timeCreate, Toast.LENGTH_SHORT).show();
                    Note note = new Note(tittle,content,timeCreate,"");
                    note.setBackgroundColor(0);
                    saveNoteToDatabase(note);
                    dialog.dismiss();
                    getController().navigateUp();
                }

            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getController().navigateUp();
            }
        });

        builder.create().show();
    }
    private void initDatabase() {
        // init room database
        database = Room.databaseBuilder(getContext(), AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        noteDao = database.getNoteDAO();
    }
    public void saveNoteToDatabase(Note note){
        noteDao.insert(note);
    }

    @Override
    public void onResume() {
        super.onResume();
        menuCallback.hiddenMenuCircle(true);
    }
}
