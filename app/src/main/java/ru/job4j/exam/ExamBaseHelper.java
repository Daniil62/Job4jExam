package ru.job4j.exam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExamBaseHelper extends SQLiteOpenHelper {
    private static final String DB = "exams.db";
    private static final int VERSION = 1;
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ExamDbSchema.ExamTable.NAME + " ("
                + "id integer primary key autoincrement, " + ExamDbSchema.ExamTable.Cols.TITLE
                + " " + ")"
        );
    }
    ExamBaseHelper(Context context) {
        super (context, DB, null, VERSION);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
