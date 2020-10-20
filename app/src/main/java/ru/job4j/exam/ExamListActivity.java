package ru.job4j.exam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class ExamListActivity extends AppCompatActivity implements DialogExamsDelete
        .ExamsDeleteDialogListener {
    private final FragmentManager manager = getSupportFragmentManager();
    private ExamBaseHelper helper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exams);
        Fragment fragment = manager.findFragmentById(R.id.list_exams);
        this.helper = new ExamBaseHelper(this);
        if (fragment == null) {
            fragment = new ExamListFragment();
            manager.beginTransaction().replace(R.id.list_exams, fragment)
                    .addToBackStack(null).commit();
        }
    }
    @Override
    public void positiveExamsDeleteClick(DialogExamsDelete ded) {
        helper.deleteExam();
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
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
