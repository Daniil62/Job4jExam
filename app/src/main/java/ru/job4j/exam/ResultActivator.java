package ru.job4j.exam;

import android.support.v4.app.Fragment;

public class ResultActivator extends ResultActivity {
    @Override
    public Fragment loadFrg() {
        return new ResultFragment();
    }
}
