package jiaqi.yanb.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import jiaqi.yanb.Constants;
import jiaqi.yanb.Constants.EditMode;
import jiaqi.yanb.Note;
import jiaqi.yanb.R;
import jiaqi.yanb.ui.EditNoteActivity;

public class RvNotesAdapter extends RecyclerView.Adapter<RvNotesAdapter.NoteHolder> {

    // The context of the adapter should be an Activity.
    private Activity mContext;

    private List<Note> mNotes;

    public class NoteHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mContent;

        // Note bound to this holder.
        private Note mNote;
        // Position in the RecyclerView
        private int mPosition;

        public NoteHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTitle = (TextView) itemView.findViewById(R.id.item_note_title);
            mDate = (TextView) itemView.findViewById(R.id.item_note_date);
            mContent = (TextView) itemView.findViewById(R.id.item_note_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext, "Note #" + mPosition + " is clicked.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, EditNoteActivity.class);
                    Bundle extras = new Bundle();
                    extras.putSerializable(Constants.EXTRA_KEY_NAME_NOTE, mNote);
                    extras.putSerializable(Constants.EXTRA_KEY_NAME_EDIT_MODE, EditMode.MODIFY);
                    intent.putExtras(extras);
                    mContext.startActivityForResult(intent, Constants.ACTIVITY_REQUEST_CODE_MODIFY);
                }
            });
        }

        public void bindHolder(int position) {
            mPosition = position;
            mNote = mNotes.get(mPosition);
            mTitle.setText(mNote.getTitle());
            mContent.setText(mNote.getContent());

            // set date text
            SimpleDateFormat sdf = new SimpleDateFormat();
            String dateString = sdf.format(mNote.getDate());
            mDate.setText(dateString);

            LayerDrawable bgRoundedCorners = (LayerDrawable) mView.getBackground();
            GradientDrawable shapeRoundedCorners = (GradientDrawable)
                    bgRoundedCorners.findDrawableByLayerId(R.id.shape_rounded_corners);
            if (position % 2 == 1) {
                shapeRoundedCorners.setColor(mContext.getResources().getColor(R.color.colorAccent));
            } else {
                shapeRoundedCorners.setColor(mContext.getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    public RvNotesAdapter(List<Note> notes, Activity context) {
        mContext = context;
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
        holder.bindHolder(position);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

}
