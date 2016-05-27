package jiaqi.yanb.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jiaqi.yanb.Note;
import jiaqi.yanb.R;

/**
 * Created by Jiaqi on 5/26/2016.
 */
public class RvNotesAdapter extends RecyclerView.Adapter<RvNotesAdapter.NoteHolder> {

    private List<Note> mNotes;

    public RvNotesAdapter(List<Note> notes) {
        mNotes = notes;
    }

    public void setNotes(List<Note> notes) {
        mNotes = notes;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        holder.mTitle.setText(mNotes.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public static class NoteHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;

        public NoteHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.item_note_title);
        }
    }

}
