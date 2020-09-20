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
                + " (" + "id integer primary key autoincrement, "
                + "exam_id integer " + ExamDbSchema.ExamTable.Cols.INDEX + ", "
                + "exam_name text " + ExamDbSchema.ExamTable.Cols.TITLE + ", "
                + "exam_date integer " + ExamDbSchema.ExamTable.Cols.TIME + ", "
                + "exam_result real " + ExamDbSchema.ExamTable.Cols.RESULT + ", "
                + "exam_mark integer " + ExamDbSchema.ExamTable.Cols.MARK
                + ");");
        db.execSQL("create table " + ExamDbSchema.QuestionTable.TAB_NAME
                + " (" + "id integer primary key autoincrement, "
                + "question_id integer " + ExamDbSchema.QuestionTable.Cols.POSITION + ", "
                + "question_text " + ExamDbSchema.QuestionTable.Cols.QUESTION_TEXT + ", "
                + "true_answer integer " + ExamDbSchema.QuestionTable.Cols.TRUE_ANSWER + ", "
                + "question_id integer reference " + ExamDbSchema.ExamTable.TAB_NAME + "(id)"
                + ");");
        db.execSQL("create table " + ExamDbSchema.AnswerTable.TAB_NAME
                + " (" + "id integer primary key autoincrement, "
                + "answer_id integer " + ExamDbSchema.AnswerTable.Cols.ANSWER_ID + ", "
                + "answer_text text " + ExamDbSchema.AnswerTable.Cols.ANSWER_TEXT + ", "
                + "answer_id integer reference " + ExamDbSchema.QuestionTable.TAB_NAME + "(id)"
                + ");");
    }
    ExamBaseHelper(Context context) {
        super (context, DB, null, VERSION);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
