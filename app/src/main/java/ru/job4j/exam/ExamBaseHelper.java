package ru.job4j.exam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExamBaseHelper extends SQLiteOpenHelper {
    private static final String DB = "j4j_exam.db";
    private static final int VERSION = 1;
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ExamDbSchema.ExamTable.TAB_NAME
                + " (" + "_id integer primary key autoincrement, "
                + ExamDbSchema.ExamTable.Cols.INDEX + " integer, "
                + ExamDbSchema.ExamTable.Cols.TITLE + " text, "
                + ExamDbSchema.ExamTable.Cols.TIME + " integer, "
                + ExamDbSchema.ExamTable.Cols.RESULT + " real, "
                + ExamDbSchema.ExamTable.Cols.MARK + " integer "
                + ")");
        db.execSQL("create table " + ExamDbSchema.QuestionTable.TAB_NAME
                + " (" + "_id integer primary key autoincrement, "
                + ExamDbSchema.QuestionTable.Cols.POSITION + " integer, "
                + ExamDbSchema.QuestionTable.Cols.QUESTION_TEXT + " text, "
                + ExamDbSchema.QuestionTable.Cols.TRUE_ANSWER + " integer, "
                + "questions_f_key integer, "
                + "foreign key " + "(" + ExamDbSchema.QuestionTable.Cols.FOREIGN_KEY + ")"
                + " references " + ExamDbSchema.ExamTable.TAB_NAME + "(_id)"
                + " on delete cascade"
                + ")");
        db.execSQL("create table " + ExamDbSchema.AnswerTable.TAB_NAME
                + " (" + "_id integer primary key autoincrement, "
                + ExamDbSchema.AnswerTable.Cols.ANSWER_ID + " integer, "
                + ExamDbSchema.AnswerTable.Cols.ANSWER_TEXT + " text, "
                + ExamDbSchema.AnswerTable.Cols.POSITION + " integer, "
                + ExamDbSchema.AnswerTable.Cols.FOREIGN_KEY + ", "
                + "foreign key " + "(" + ExamDbSchema.AnswerTable.Cols.FOREIGN_KEY + ")"
                + " references " + ExamDbSchema.QuestionTable.TAB_NAME + "(_id)"
                + " on delete cascade"
                + ")");
    }
    ExamBaseHelper(Context context) {
        super (context, DB, null, VERSION);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
