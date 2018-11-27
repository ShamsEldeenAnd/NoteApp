package com.developer.shams.noteapp.Adapter;

import android.support.annotation.NonNull;
import android.support.text.emoji.widget.EmojiTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.shams.noteapp.DataBase.Note;
import com.developer.shams.noteapp.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes;
    private OnItemClickListner onItemClickListner;

    public NoteAdapter(List<Note> notes, OnItemClickListner onItemClickListner) {
        this.notes = notes;
        this.onItemClickListner = onItemClickListner;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_row, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.title.setText(currentNote.getTitle());
        String desc = currentNote.getDescription();
        if (desc.length() > 15) {
            desc = desc.substring(0, 10);
            desc += "....";
        }
        holder.desc.setText(desc);
        holder.priority.setText(String.valueOf(currentNote.getProirity()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Note getNote(int position) {
        return notes.get(position);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        EmojiTextView title, desc, priority;

        public NoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            priority = itemView.findViewById(R.id.priority);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
                onItemClickListner.OnItimClick(notes.get(position));
        }
    }

    public interface OnItemClickListner {
        void OnItimClick(Note note);
    }
}
