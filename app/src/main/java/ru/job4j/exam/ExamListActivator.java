package ru.job4j.exam;

import android.support.v4.app.Fragment;

public class ExamListActivator extends ExamListActivity {
    @Override
    public Fragment loadFrg() {
        return ExamListFragment.of(getIntent().getIntExtra(MainActivity.MAIN_FOR, 0));
    }
}
