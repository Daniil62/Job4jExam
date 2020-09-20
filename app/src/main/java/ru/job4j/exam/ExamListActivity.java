package ru.job4j.exam;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class ExamListActivity extends AppCompatActivity implements DialogExamsDelete
        .ExamsDeleteDialogListener {
    private final FragmentManager manager = getSupportFragmentManager();
    private SQLiteDatabase store;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exams);
        Fragment fragment = manager.findFragmentById(R.id.list_exams);
        this.store = new ExamBaseHelper(this).getWritableDatabase();
        if (fragment == null) {
            fragment = new ExamListFragment();
            manager.beginTransaction().add(R.id.list_exams, fragment).commit();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public void positiveExamsDeleteClick(DialogExamsDelete ded) {
        Cursor cursor = this.store.query(ExamDbSchema.ExamTable.TAB_NAME, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndex("exam_mark")) != 0) {
                store.delete(ExamDbSchema.ExamTable.TAB_NAME, "id = "
                        + cursor.getInt(cursor.getColumnIndex("id")), new String[]{});
            }
            cursor.moveToNext();
        }
        cursor.close();
        FragmentManager manager = this.getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.list_exams, new ExamListFragment())
                .addToBackStack(null).commit();
    }
    @Override
    public void negativeExamsDeleteClick(DialogExamsDelete ded) {
        FragmentManager manager = this.getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.list_exams, new ExamListFragment())
                .addToBackStack(null).commit();
    }
}
