package ru.job4j.exam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public abstract class ExamListActivity extends FragmentActivity {
    public static final String EXAM_LIST_FOR = "exam_list_for";
    public abstract Fragment loadFrg();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_frg);
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.content) == null) {
            manager.beginTransaction().add(R.id.content, loadFrg()).commit();
        }
    }
}
