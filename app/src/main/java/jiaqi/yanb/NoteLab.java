package jiaqi.yanb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jiaqi.yanb.db.NoteDbHelper;
import jiaqi.yanb.db.NoteDbSchema;
import jiaqi.yanb.db.NoteDbSchema.NoteTable;

/**
 * Created by Jiaqi on 5/27/2016.
 */
public class NoteLab {
    private static NoteLab mNoteLab;

    private Context mContext;
//    private List<Note> mNotes;
    private SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    private NoteLab(Context context) {
        mContext = context;
//        mNotes = new ArrayList<>();
        mDatabaseHelper = new NoteDbHelper(mContext);
        mDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public static NoteLab getInstance(Context context) {
        if (mNoteLab == null) {
            mNoteLab = new NoteLab(context);
        }
        return mNoteLab;
    }

    public List<Note> getNotes() {
        List<Note> notes = getNotesFromCursor(queryFromDb(null, null));
        return notes;
    }

    public void addNote(Note note) {
        ContentValues values = getContentValues(note);
        mDatabase.insert(NoteTable.NAME, null, values);
    }

    public void updateNote(Note note) {
        String uuidString = note.getId().toString();
        ContentValues values = getContentValues(note);

        mDatabase.update(
                NoteTable.NAME,
                values,
                NoteTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.UUID, note.getId().toString());
        values.put(NoteTable.Cols.TITLE, note.getTitle());
        values.put(NoteTable.Cols.CONTENT, note.getContent());
        values.put(NoteTable.Cols.DATE, note.getDate().getTime());

        return values;
    }

    private Cursor queryFromDb(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NoteTable.NAME,
                null,           // columns, use null because we want all
                whereClause,
                whereArgs,
                null,           // groupBy, we don't care
                null,           // having, we don't care
                null);          // orderBy, we don't care
        return cursor;
    }

    private List<Note> getNotesFromCursor(Cursor cursor) {
        List<Note> notes = new ArrayList<>();

        Note note;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String uuidString = cursor.getString(cursor.getColumnIndex(NoteTable.Cols.UUID));
            String title = cursor.getString(cursor.getColumnIndex(NoteTable.Cols.TITLE));
            String content = cursor.getString(cursor.getColumnIndex(NoteTable.Cols.CONTENT));
            long date = cursor.getLong(cursor.getColumnIndex(NoteTable.Cols.DATE));

            note = new Note(
                    UUID.fromString(uuidString),
                    title,
                    content,
                    new Date(date));

            notes.add(note);

            cursor.moveToNext();
        }

        return notes;
    }
}
