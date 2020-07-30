package com.example.notesmasteer.adapter;

import com.example.notesmasteer.BR;
import com.example.notesmasteer.R;
import com.example.notesmasteer.base.BaseAdapter;
import com.example.notesmasteer.base.CBAdapter;
import com.example.notesmasteer.callback.NoteCallBack;
import com.example.notesmasteer.databinding.ItemNoteBinding;
import com.example.notesmasteer.model.Note;

public class NoteAdapter  extends BaseAdapter<Note, ItemNoteBinding> {
    NoteCallBack callBack;
    @Override
    public int getLayoutId() {
        return R.layout.item_note;
    }

    @Override
    public int getIdVariable() {
        return BR.note;
    }

    @Override
    public int getIdVariableOnclick() {
        return BR.callback;
    }

    @Override
    public CBAdapter getOnclick() {
        return callBack;
    }

    public void setCallBack(NoteCallBack callBack) {
        this.callBack = callBack;
    }
}
