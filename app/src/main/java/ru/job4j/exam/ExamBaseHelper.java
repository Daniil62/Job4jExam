package ru.job4j.exam;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import ru.job4j.exam.store.QuestionStore;

public class ExamBaseHelper extends SQLiteOpenHelper implements StoreManager {
    private SQLiteDatabase db = this.getReadableDatabase();
    private static final String DB = "j4j_exam.db";
    private static final int VERSION = 1;
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ExamDbSchema.ExamTable.TAB_NAME
                + " (" + "_id integer primary key autoincrement, "
                + ExamDbSchema.ExamTable.Cols.INDEX + " integer, "
                + ExamDbSchema.ExamTable.Cols.TITLE + " text, "
                + ExamDbSchema.ExamTable.Cols.DATE + " integer, "
                + ExamDbSchema.ExamTable.Cols.RESULT + " real, "
                + ExamDbSchema.ExamTable.Cols.MARK + " integer, "
                + ExamDbSchema.ExamTable.Cols.DESC + " text, "
                + ExamDbSchema.ExamTable.Cols.TIME_RESULT + " integer"
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
    QuestionStore setQuestionStore (int id) {
        QuestionStore result = new QuestionStore();
        Cursor cursor = db.query(ExamDbSchema.QuestionTable.TAB_NAME, null,
                ExamDbSchema.QuestionTable.Cols.FOREIGN_KEY + " = " + id,
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex(
                    ExamDbSchema.QuestionTable.Cols.FOREIGN_KEY)) == id) {
                Question question = new Question(cursor.getInt(
                        cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.POSITION)),
                        cursor.getString(cursor.getColumnIndex(
                                ExamDbSchema.QuestionTable.Cols.QUESTION_TEXT)), this.setAnswers(
                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getInt(cursor.getColumnIndex(
                                ExamDbSchema.QuestionTable.Cols.POSITION))),
                        cursor.getInt(cursor.getColumnIndex(
                                ExamDbSchema.QuestionTable.Cols.TRUE_ANSWER)));
                result.addQuestion(question);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }
    private List<Option> setAnswers(int id, int group) {
        Cursor cursor = db.query(ExamDbSchema.AnswerTable.TAB_NAME, null,
                ExamDbSchema.AnswerTable.Cols.FOREIGN_KEY + " = " + id + " and "
                        + ExamDbSchema.AnswerTable.Cols.POSITION + " = " + group,
                null, null, null, null);
        List<Option> result = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex(
                    ExamDbSchema.AnswerTable.Cols.FOREIGN_KEY)) == id
                    && cursor.getInt(cursor.getColumnIndex(
                    ExamDbSchema.AnswerTable.Cols.POSITION)) == group) {
                Option option = new Option(cursor.getInt(cursor.getColumnIndex(
                        ExamDbSchema.AnswerTable.Cols.ANSWER_ID)),
                        cursor.getString(cursor.getColumnIndex(
                                ExamDbSchema.AnswerTable.Cols.ANSWER_TEXT)),
                        cursor.getInt(cursor.getColumnIndex(
                                ExamDbSchema.AnswerTable.Cols.POSITION)));
                result.add(option);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }
    int getTrueAnswer(int id, int position) {
        int result = -1;
        SQLiteDatabase store = this.getReadableDatabase();
        Cursor cursor = store.query(ExamDbSchema.QuestionTable.TAB_NAME, null,
                ExamDbSchema.QuestionTable.Cols.FOREIGN_KEY + " = " + id + " and "
                        + ExamDbSchema.QuestionTable.Cols.POSITION
                        + " = " + position,
                null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            if (cursor.getInt(
                    cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.FOREIGN_KEY)) == id &&
                    cursor.getInt(cursor.getColumnIndex(
                            ExamDbSchema.QuestionTable.Cols.POSITION)) == position) {
                result = cursor.getInt(
                        cursor.getColumnIndex(ExamDbSchema.QuestionTable.Cols.TRUE_ANSWER));
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }
    void buildExam(QuestionStore qs, int groupSize) {
        ContentValues values = new ContentValues();
        values.put(ExamDbSchema.ExamTable.Cols.TITLE, qs.getExam().getName());
        values.put(ExamDbSchema.ExamTable.Cols.MARK, 0);
        values.put(ExamDbSchema.ExamTable.Cols.DESC, qs.getExam().getDesc());
        db.insert(ExamDbSchema.ExamTable.TAB_NAME, null, values);
        values.clear();
        Cursor cursor = db.query(ExamDbSchema.ExamTable.TAB_NAME, null,
                null, null, null, null, null);
        cursor.moveToLast();
        int idOfExam = cursor.getInt(cursor.getColumnIndex("_id"));
        cursor.close();
        for (Question q : qs.getQuestions()) {
            String s = q.getText();
            if (!s.equals("")) {
                values.put(ExamDbSchema.QuestionTable.Cols.FOREIGN_KEY, idOfExam);
                values.put(ExamDbSchema.QuestionTable.Cols.QUESTION_TEXT, q.getText());
                values.put(ExamDbSchema.QuestionTable.Cols.POSITION, q.getId());
                values.put(ExamDbSchema.QuestionTable.Cols.TRUE_ANSWER, q.getAnswer());
                db.insert(ExamDbSchema.QuestionTable.TAB_NAME, null, values);
            }
            setAnswerTable(q, groupSize);
            if (qs.answersSize() > groupSize) {
                groupSize += 4;
            }
        }
    }
    List<Exam> getExams() {
        List<Exam> exams = new ArrayList<>();
        Cursor cursor = db.query(ExamDbSchema.ExamTable.TAB_NAME, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            exams.add(new Exam(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.TITLE)),
                    cursor.getLong(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DATE)),
                    cursor.getFloat(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.RESULT)),
                    cursor.getInt(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.MARK)) > 0,
                    cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DESC)),
                    cursor.getLong(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.TIME_RESULT)))
            );
            cursor.moveToNext();
        }
        cursor.close();
        return exams;
    }
    private void setAnswerTable(Question question, int groupSize) {
        ContentValues values = new ContentValues();
        @SuppressLint("Recycle")
        Cursor cursor = db.query(ExamDbSchema.QuestionTable.TAB_NAME, null,
                null, null, null, null, null);
        cursor.moveToLast();
        int idOfQuestions = cursor.getInt(cursor.getColumnIndex("_id"));
        for (int i = 0; i < groupSize; i++) {
            Option o = question.getOptions().get(i);
            values.put(ExamDbSchema.AnswerTable.Cols.FOREIGN_KEY, idOfQuestions);
            values.put(ExamDbSchema.AnswerTable.Cols.ANSWER_TEXT, o.getText());
            values.put(ExamDbSchema.AnswerTable.Cols.POSITION, o.getGroup());
            values.put(ExamDbSchema.AnswerTable.Cols.ANSWER_ID, o.getId());
            db.insert(ExamDbSchema.AnswerTable.TAB_NAME, null, values);
            values.clear();
        }
        cursor.close();
    }
    @Override
    public String getExamDescription(int id) {
        Cursor cursor = db.query(ExamDbSchema.ExamTable.TAB_NAME, null,
                "_id = " + id, null, null, null, null);
        String result = "";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex("_id")) == id) {
                result = cursor.getString(cursor.getColumnIndex(ExamDbSchema.ExamTable.Cols.DESC));
            }
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }
    void deleteExam() {
        Cursor cursor = this.db.query(ExamDbSchema.ExamTable.TAB_NAME, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex("exam_mark")) != 0) {
                db.delete(ExamDbSchema.ExamTable.TAB_NAME, "_id = "
                        + cursor.getInt(cursor.getColumnIndex("_id")), new String[]{});
            }
            cursor.moveToNext();
        }
        cursor.close();
    }
    void renameExam(int id, String name) {
        ContentValues values = new ContentValues();
        values.put(ExamDbSchema.ExamTable.Cols.TITLE, name);
        db.update(ExamDbSchema.ExamTable.TAB_NAME, values, "_id = ?",
                new String[] {String.valueOf(id)});
    }
}
