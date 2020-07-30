package com.example.notesmasteer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesmasteer.R;
import com.example.notesmasteer.callback.NoteCallBack;
import com.example.notesmasteer.model.Note;

import java.util.ArrayList;

public class Note2Adapter extends RecyclerView.Adapter<Note2Adapter.MyviewHoder> {
    ArrayList<Note> list ;
    NoteCallBack listener;
    @NonNull
    @Override
    public MyviewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nodes,parent,false);
        return new MyviewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHoder holder, int position) {
            final Note note = list.get(position);
            holder.bindData(note);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        listener.onNoteClick(note);
                    }
                }
            });
    }

    public void setListener(NoteCallBack listener) {
        this.listener = listener;
    }

    public  void setList(ArrayList<Note> arr){
        list = arr;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyviewHoder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvContent,tvTimcreate;
        public MyviewHoder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitles);
            tvContent = (TextView) itemView.findViewById(R.id.tvContents);
            tvTimcreate = (TextView) itemView.findViewById(R.id.tvTimeCreates);
        }
        public void bindData(Note note){
            tvTitle.setText(note.getTitle());
            tvContent.setText(note.getContent());
            tvTimcreate.setText(note.getTimeCreate());
        }
    }
}
