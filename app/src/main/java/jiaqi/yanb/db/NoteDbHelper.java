package jiaqi.yanb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import jiaqi.yanb.db.NoteDbSchema.NoteTable;

/**
 * Created by Jiaqi on 5/27/2016.
 */
public class NoteDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_FILENAME = "noteDatebase.db";

    public NoteDbHelper(Context context) {
        super(context, DATABASE_FILENAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NoteTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                NoteTable.Cols.UUID + ", " +
                NoteTable.Cols.TITLE + ", " +
                NoteTable.Cols.CONTENT + ", " +
                NoteTable.Cols.DATE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
